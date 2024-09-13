package com.chess.engine.board;

import com.chess.engine.pieces.Piece;

public abstract class Move {
    
    final Board board;
    final Piece movedPlace;
    final int destinationCoordinate;

    Move(final Board board, final Piece movedPlace, final int destinationCoordinate) {
        this.board = board;
        this.movedPlace = movedPlace;
        this.destinationCoordinate = destinationCoordinate;
    }

    public static final class MajorMove extends Move {

        public MajorMove(final Board board, final Piece movedPlace, final int destinationCoordinate) {
            super(board, movedPlace, destinationCoordinate);
        }

    }

    public static final class AttackMove extends Move {

        private Piece attackedPiece;

        public AttackMove(final Board board, final Piece movedPlace, final int destinationCoordinate, final Piece attackedPiece) {
            super(board, movedPlace, destinationCoordinate);
            this.attackedPiece = attackedPiece;
        }

    }
}
