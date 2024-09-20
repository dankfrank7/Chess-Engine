package com.chess.engine.pieces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.google.common.collect.ImmutableList;

public class Pawn extends Piece {

    private final static int[] CANDIDATE_MOVE_COORDINATES = {8, 16, 7, 9};
    
    public Pawn(int piecePosition, Alliance pieceAlliance) {
        super(piecePosition, pieceAlliance);
    }

    public Pawn(int piecePosition, Alliance black) {
        //TODO Auto-generated constructor stub
    }

    public Pawn(int piecePosition, Alliance white) {
        //TODO Auto-generated constructor stub
    }

    public Pawn(int piecePosition, Alliance white) {
        //TODO Auto-generated constructor stub
    }

    @Override
    public Collection<Move> calculateLegalMoves(Board board) {
        
        final List<Move> legalMoves = new ArrayList<>();
        
        for (final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATES) {
            
            int candidateDestinationCoordinate = this.piecePosition + this.pieceAlliance.getDirection() * currentCandidateOffset;

            if (!BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
                continue;
            }

            // normal move forward
            if (currentCandidateOffset == 8 && !board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
                // to do: implement promotions, en passent, 
                // to do: implement pawn move here
                legalMoves.add(new Move.MajorMove(board, this, candidateDestinationCoordinate));
            
            // first move jump
            } else if (currentCandidateOffset == 16 && this.isFirstMove() && 
                (BoardUtils.SECOND_ROW[this.piecePosition] && this.getPieceAlliance().isBlack()) ||
                (BoardUtils.SEVENTH_ROW[this.piecePosition] && this.getPieceAlliance().isWhite())) {
                // pawn jump first move
                final int behindCandidateDestinationCoordinate = this.piecePosition + (this.pieceAlliance.getDirection() * 8);
                if (!board.getTile(behindCandidateDestinationCoordinate).isTileOccupied() && 
                    !board.getTile(candidateDestinationCoordinate).isTileOccupied()) { 
                        // to do: implement jump pawn move here
                        legalMoves.add(new Move.MajorMove(board, this, candidateDestinationCoordinate));
                    }
            
            // attacking moves
            } else if (currentCandidateOffset == 7 && 
                        ((BoardUtils.EIGHTH_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite()) || 
                        (BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack()))) {

                if (board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
                    final Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();
                    if (this.pieceAlliance != pieceOnCandidate.getPieceAlliance()) {
                        // To do: add attack move
                        legalMoves.add(new Move.MajorMove(board, pieceOnCandidate, candidateDestinationCoordinate));
                    }
                }
            } else if (currentCandidateOffset == 9 &&
                ((BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite()) || 
                (BoardUtils.EIGHTH_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack()))) {
                    final Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();
                    if (this.pieceAlliance != pieceOnCandidate.getPieceAlliance()) {
                        // To do: add attack move
                        legalMoves.add(new Move.MajorMove(board, pieceOnCandidate, candidateDestinationCoordinate));
                    }
            }
         }
    return ImmutableList.copyOf(legalMoves);
    }   
}
