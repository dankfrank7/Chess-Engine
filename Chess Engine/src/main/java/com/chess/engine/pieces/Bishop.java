package com.chess.engine.pieces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.google.common.collect.ImmutableList;

public class Bishop extends Piece {

    private final static int[] CANDIDATE_MOVE_VECTOR_COORDINATES = {-9, -7, 7, 9};

    public Bishop(int piecePosition, Alliance pieceAlliance) {
        super(PieceType.BISHOP, piecePosition, pieceAlliance);
    }

    @Override
    public Bishop movePiece(Move move) {
        return new Bishop(move.getDestinationCoordinate(), move.getMovedPiece().getPieceAlliance());
    }
    
    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {
        final List<Move> legalMoves = new ArrayList<>();

        for(final int currentCandidateOffset : CANDIDATE_MOVE_VECTOR_COORDINATES) {
            int candidateDestinationCoordinate = this.piecePosition;

            while(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
                
                if (isFirstColumnExclusion(candidateDestinationCoordinate, currentCandidateOffset) ||
                isEighthColumnExclusion(candidateDestinationCoordinate, currentCandidateOffset)) {
                    break;
                }
                
                candidateDestinationCoordinate += currentCandidateOffset;

                if (BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
                    final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);

                    if (!candidateDestinationTile.isTileOccupied()) {
                        legalMoves.add(new Move.MajorMove(board, this, candidateDestinationCoordinate));
                    } else { 
                        final Piece pieceAtDestination = candidateDestinationTile.getPiece();
                        final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();

                        if (this.pieceAlliance != pieceAlliance) {
                            legalMoves.add(new Move.AttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination));
                        }
                        break; // if tile occupied, we can't continue past it
                    }
                }
            }
        }
        return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public String toString() {
        return PieceType.BISHOP.toString();
    }

    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidtateOffset) {
        return BoardUtils.FIRST_COLUMN[currentPosition] && (candidtateOffset == -9 || candidtateOffset == 7);
    }

    private static boolean isEighthColumnExclusion(final int currentPosition, final int candidtateOffset) {
        return BoardUtils.EIGHTH_COLUMN[currentPosition] && (candidtateOffset == 9 || candidtateOffset ==-7);
    }


}
