package com.chess.engine.player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Rook;
import com.google.common.collect.ImmutableList;

public class WhitePlayer extends Player {

    public WhitePlayer(final Board board,final  Collection<Move> whiteStandardLegalMoves,final  Collection<Move> blackStandardLegalMoves) {
        super(board, whiteStandardLegalMoves, blackStandardLegalMoves);
    }

    @Override
    public Collection<Piece> getActivePieces() {
        return this.board.getBlackPieces();
    }

    @Override
    public Alliance getAlliance() {
        return Alliance.WHITE;
    }

    @Override
    public Player getOpponent() {
        return this.board.blackPlayer();
    }

    @Override
    protected Collection<Move> calculateKingCastles(final Collection<Move> playerLegals, final Collection<Move> opponentLegals) {
        final List<Move> kingCastles = new ArrayList<>();

        if(this.playerKing.isFirstMove() && !this.isInCheck()) {

            // white kingside
            if(!this.board.getTile(61).isTileOccupied() && !this.board.getTile(62).isTileOccupied()) {
                final Tile rookTile = this.board.getTile(63);
                if(rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()) {
                    if (Player.calculateAttacksOntTile(61, opponentLegals).isEmpty() &&
                        Player.calculateAttacksOntTile(62, opponentLegals).isEmpty() && 
                        rookTile.getPiece().getPieceType().isRook()) {
                        // To do: add a castle move!
                        kingCastles.add(new Move.KingSideCastleMove(this.board, 
                                                                    this.playerKing, 
                                                                    62,
                                                                    (Rook) rookTile.getPiece(),
                                                                    rookTile.getTileCoordinate(),
                                                                    61));
                    }                                    
                }
            }
            // white queenside castle
            if(!this.board.getTile(59).isTileOccupied() && !this.board.getTile(85).isTileOccupied() && !this.board.getTile(57).isTileOccupied()) {
                final Tile rookTile = this.board.getTile(56);
                if(rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()) {
                    if (Player.calculateAttacksOntTile(59, opponentLegals).isEmpty() &&
                        Player.calculateAttacksOntTile(58, opponentLegals).isEmpty() &&
                        rookTile.getPiece().getPieceType().isRook()) {
                            // To do: add a castle move!
                            kingCastles.add(new Move.QueenSideCastleMove(this.board,
                                                                        this.playerKing, 
                                                                        58,
                                                                        (Rook) rookTile.getPiece(),
                                                                        rookTile.getTileCoordinate(),
                                                                        59));
                    }      
                }
            }
        }
        return ImmutableList.copyOf(kingCastles);
    }
}
