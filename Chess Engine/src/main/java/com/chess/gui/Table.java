package com.chess.gui;

import javax.imageio.ImageIO;
import javax.swing.*;

import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.chess.engine.pieces.Piece;
import com.chess.engine.player.MoveTransition;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import javax.swing.SwingUtilities;

public class Table {
    
    private final JFrame gameFrame; 
    private final BoardPanel boardPanel;
    private Board chessBoard;

    private Tile sourceTile;
    private Tile destinationTile;
    private Piece humanMovedPiece;

    public final static Dimension OUTER_FRAME_DIMENSION = new Dimension(600,600);
    private static final Dimension BOARD_PANEL_DIMENSION = new Dimension(400, 350);
    private static final Dimension TILE_PANEL_DIMENSION = new Dimension(10, 10);

    private static String defaultPieceImagesPath = "Chess Engine\\art\\pieces\\plain\\";

    private Color lightTileColor = Color.decode("#FFFACD");
    private Color darkTileColor = Color.decode("#593E1A");

    public Table() { 
        
        // frame 
        this.gameFrame = new JFrame("JChess");
        this.gameFrame.setLayout(new BorderLayout());
        final JMenuBar tableMenuBar = createTableMenuBar();
        this.gameFrame.setJMenuBar(tableMenuBar);
        this.gameFrame.setSize(OUTER_FRAME_DIMENSION);
    
        // board
        this.chessBoard = Board.createStandardBoard();
        this.boardPanel = new BoardPanel();
        this.gameFrame.add(this.boardPanel, BorderLayout.CENTER);

        this.gameFrame.setVisible(true);
    }

    private JMenuBar createTableMenuBar() {
        final JMenuBar tableMenuBar = new JMenuBar();
        tableMenuBar.add(createFileMenu());
        return tableMenuBar;
    }

    private JMenu createFileMenu() {
        final JMenu fileMenu = new JMenu("File");
        final JMenuItem openPGN = new JMenuItem("Load PGN File");   
        openPGN.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Open up that PGN file!");
            } 
        });
        fileMenu.add(openPGN);

        final JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }; 
        });
        fileMenu.add(exitMenuItem);

        return fileMenu;
    }

    private class BoardPanel extends JPanel {
        final List<TilePanel> boardTiles;
    
        BoardPanel() {
            super(new GridLayout(8,8));
            this.boardTiles = new ArrayList<>();
            for(int i = 0; i < BoardUtils.NUM_TILES; i++) {
                final TilePanel tilePanel = new TilePanel(this, i);
                this.boardTiles.add(tilePanel);
                add(tilePanel);
            }
            setPreferredSize(BOARD_PANEL_DIMENSION);
        }

        public void drawBoard(final Board board) {
            removeAll();
            for (final TilePanel tilePanel : boardTiles) {
                tilePanel.drawTile(board);
                add(tilePanel);
            }
            validate();
            repaint();
        }
    }

    private class TilePanel extends JPanel {
        private final int tileId;

        TilePanel(final BoardPanel boardPanel, final int tileId) {
            super(new GridBagLayout());
            this.tileId = tileId;
            setPreferredSize(TILE_PANEL_DIMENSION);
            assignTileColour();
            assignTilePieceIcon(chessBoard);
            validate();

            addMouseListener(new MouseListener() {

                @Override
                public void mouseClicked(final MouseEvent e) {
                    System.out.print("Mouse clicked: ");

                    if (SwingUtilities.isRightMouseButton(e)) {
                        System.out.println("RIGHT MOUSE on_ " + tileId);

                        sourceTile = null;
                        destinationTile = null;
                        humanMovedPiece = null;

                    } else if (SwingUtilities.isLeftMouseButton(e)) {
                        System.out.println("LEFT MOUSE on_ " + tileId);

                        if(sourceTile == null) {
                            // first click
                            sourceTile = chessBoard.getTile(tileId);
                            humanMovedPiece = sourceTile.getPiece();
                            // undo if clicks an empty tile
                            if (humanMovedPiece == null) {
                                sourceTile = null;
                            }
                        } else {
                            // second click
                            destinationTile = chessBoard.getTile(tileId);
                            final Move move = Move.MoveFactory.createMove(chessBoard, sourceTile.getTileCoordinate(), destinationTile.getTileCoordinate());
                            final MoveTransition transition = chessBoard.currentPlayer().makeMove(move);
                            if (transition.getMoveStatus().isDone()) {
                                chessBoard = transition.getBoard();
                                // TODO add move to the move log
                            }
                            sourceTile = null;
                            destinationTile = null;
                            humanMovedPiece = null;
                        }
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                boardPanel.drawBoard(chessBoard);
                            }
                        });
                    }
                }

                @Override
                public void mouseEntered(final MouseEvent e) {
                    // TODO Auto-generated method stub
                    // throw new UnsupportedOperationException("Unimplemented method 'mouseEntered'");
                }

                @Override
                public void mouseExited(final MouseEvent e) {
                    // TODO Auto-generated method stub
                    // throw new UnsupportedOperationException("Unimplemented method 'mouseExited'");
                }

                @Override
                public void mousePressed(final MouseEvent e) {
                    // TODO Auto-generated method stub
                    // throw new UnsupportedOperationException("Unimplemented method 'mousePressed'");
                }

                @Override
                public void mouseReleased(final MouseEvent e) {
                    // TODO Auto-generated method stub
                    // throw new UnsupportedOperationException("Unimplemented method 'mouseReleased'");
                }

            });
        }

        public void drawTile(final Board board) {
            assignTileColour();
            assignTilePieceIcon(board);
            // TODO add board gui visuals e.g highlighting  
            validate();
        }

        private void assignTilePieceIcon(final Board board) {
            this.removeAll();
            if (board.getTile(this.tileId).isTileOccupied()) {
                try {
                    // load image e.g WB.gif
                    final BufferedImage image = ImageIO.read(new File(defaultPieceImagesPath  
                                                             + board.getTile(this.tileId).getPiece().getPieceAlliance().toString().substring(0, 1)
                                                             + board.getTile(this.tileId).getPiece().toString()
                                                             + ".gif"));
                    add(new JLabel(new ImageIcon(image)));
                } catch (IOException e) {
                    System.out.println("Can't find images!");
                    e.printStackTrace();
                }
            }
        }

        private void assignTileColour() {
            if (BoardUtils.EIGHTH_RANK[this.tileId] ||
                BoardUtils.SIXTH_RANK[this.tileId] ||
                BoardUtils.FOURTH_RANK[this.tileId] ||
                BoardUtils.SECOND_RANK[this.tileId]) {
                setBackground(this.tileId % 2 == 0 ? lightTileColor : darkTileColor);
            } else if(BoardUtils.SEVENTH_RANK[this.tileId] ||
                      BoardUtils.FIFTH_RANK[this.tileId] ||
                      BoardUtils.THIRD_RANK[this.tileId]  ||
                      BoardUtils.FIRST_RANK[this.tileId]) {
                setBackground(this.tileId % 2 != 0 ? lightTileColor : darkTileColor);
            }
        }
    }
}
