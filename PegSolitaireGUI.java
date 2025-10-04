import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.*;

public class PegSolitaireGUI extends JFrame {

    private PegBoard board;
    private PegSolitaire game; // Reference to the game logic
    private BoardPanel boardPanel;
    private JLabel messageLabel;

    private Location selectedPeg = null; // To store the currently selected peg for a jump

    public PegSolitaireGUI(PegSolitaire game) {
        this.game = game;
        this.board = game.getBoard(); // Assuming PegSolitaire has a getBoard() method

        setTitle("Peg Solitaire");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // Main panel to hold the board and messages
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Board panel
        boardPanel = new BoardPanel();
        mainPanel.add(boardPanel, BorderLayout.CENTER);

        // Message label
        messageLabel = new JLabel("Welcome to Peg Solitaire! Select a peg to move.", SwingConstants.CENTER);
        messageLabel.setFont(new Font("Arial", Font.BOLD, 16));
        mainPanel.add(messageLabel, BorderLayout.SOUTH);

        add(mainPanel);
        pack();
        setLocationRelativeTo(null); // Center the window
    }

    // Inner class for drawing the board
    private class BoardPanel extends JPanel {
        private final int CELL_SIZE = 60;
        private final int BOARD_OFFSET_X = 50;
        private final int BOARD_OFFSET_Y = 50;

        public BoardPanel() {
            setPreferredSize(new Dimension(BOARD_SIZE * CELL_SIZE + 2 * BOARD_OFFSET_X,
                    BOARD_SIZE * CELL_SIZE + 2 * BOARD_OFFSET_Y));
            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    int col = (e.getX() - BOARD_OFFSET_X) / CELL_SIZE;
                    int row = (e.getY() - BOARD_OFFSET_Y) / CELL_SIZE;

                    handleMouseClick(row, col);
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Draw the board background
            g2d.setColor(new Color(139, 69, 19)); // Brown color for the board
            g2d.fillRect(0, 0, getWidth(), getHeight());

            // Draw the grid and pegs
            for (int row = 0; row < board.getBoardSize(); row++) {
                for (int col = 0; col < board.getBoardSize(); col++) {
                    if (board.isValidLocation(row, col)) {
                        int x = col * CELL_SIZE + BOARD_OFFSET_X;
                        int y = row * CELL_SIZE + BOARD_OFFSET_Y;

                        // Draw cell background
                        g2d.setColor(Color.LIGHT_GRAY);
                        g2d.fillRect(x, y, CELL_SIZE, CELL_SIZE);
                        g2d.setColor(Color.BLACK);
                        g2d.drawRect(x, y, CELL_SIZE, CELL_SIZE);

                        // Draw peg if present
                        if (board.isPeg(row, col)) {
                            g2d.setColor(Color.RED);
                            g2d.fillOval(x + 5, y + 5, CELL_SIZE - 10, CELL_SIZE - 10);
                            g2d.setColor(Color.BLACK);
                            g2d.drawOval(x + 5, y + 5, CELL_SIZE - 10, CELL_SIZE - 10);
                        }

                        // Highlight selected peg
                        if (selectedPeg != null && selectedPeg.getRow() == row && selectedPeg.getCol() == col) {
                            g2d.setColor(Color.BLUE);
                            g2d.setStroke(new BasicStroke(3));
                            g2d.drawRect(x + 1, y + 1, CELL_SIZE - 2, CELL_SIZE - 2);
                            g2d.setStroke(new BasicStroke(1));
                        }
                    }
                }
            }
        }
    }

    private void handleMouseClick(int row, int col) {
        if (!board.isValidLocation(row, col)) {
            messageLabel.setText("Invalid board location. Please click within the board.");
            return;
        }

        if (selectedPeg == null) {
            // No peg selected yet, try to select one
            if (board.isPeg(row, col)) {
                ArrayList<Location> validMoves = game.openValidLocations(row, col);
                if (!validMoves.isEmpty()) {
                    selectedPeg = new Location(row, col);
                    messageLabel
                            .setText("Peg selected at (" + row + ", " + col + "). Now select an empty destination.");
                } else {
                    messageLabel.setText("Peg at (" + row + ", " + col + ") has no valid moves. Select another peg.");
                }
            } else {
                messageLabel.setText("No peg at (" + row + ", " + col + "). Please select a peg.");
            }
        } else {
            // A peg is already selected, try to make a jump
            if (!board.isPeg(row, col)) { // Destination must be empty
                // Check if this is a valid jump destination for the selected peg
                ArrayList<Location> validMoves = game.openValidLocations(selectedPeg.getRow(), selectedPeg.getCol());
                boolean isValidDestination = false;
                for (Location loc : validMoves) {
                    if (loc.getRow() == row && loc.getCol() == col) {
                        isValidDestination = true;
                        break;
                    }
                }

                if (isValidDestination) {
                    // Perform the jump
                    game.playPeg(selectedPeg.getRow(), selectedPeg.getCol(), new Location(row, col)); // Assuming
                                                                                                      // playPeg can
                                                                                                      // take a
                                                                                                      // destination
                    selectedPeg = null; // Reset selected peg
                    updateGameStatus();
                } else {
                    messageLabel.setText("Invalid destination for peg at (" + selectedPeg.getRow() + ", "
                            + selectedPeg.getCol() + "). Try again.");
                }
            } else {
                // Clicked on another peg, re-select
                ArrayList<Location> validMoves = game.openValidLocations(row, col);
                if (!validMoves.isEmpty()) {
                    selectedPeg = new Location(row, col);
                    messageLabel
                            .setText("Peg re-selected at (" + row + ", " + col + "). Now select an empty destination.");
                } else {
                    messageLabel.setText("Peg at (" + row + ", " + col + ") has no valid moves. Select another peg.");
                    selectedPeg = null; // Deselect if new peg has no moves
                }
            }
        }
        boardPanel.repaint(); // Redraw the board to reflect changes
    }

    private void updateGameStatus() {
        boardPanel.repaint();
        int pegCount = board.pegCount();
        if (pegCount == 1) {
            messageLabel.setText("Congratulations! You won with 1 peg remaining!");
            showGameOverDialog("You Won!", "Congratulations! You finished with 1 peg remaining.");
        } else if (!game.hasValidMove()) {
            messageLabel.setText("Game Over! No more valid moves. Your score: " + pegCount + " pegs remaining.");
            showGameOverDialog("Game Over", "No more valid moves. Your score: " + pegCount + " pegs remaining.");
        } else {
            messageLabel.setText("Pegs remaining: " + pegCount + ". Select a peg to move.");
        }
    }

    private void showGameOverDialog(String title, String message) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
        // Optionally, offer to restart the game
        int choice = JOptionPane.showConfirmDialog(this, "Do you want to play again?", "Play Again?",
                JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            game.resetGame(); // Assuming a resetGame method exists
            selectedPeg = null;
            updateGameStatus();
        } else {
            System.exit(0);
        }
    }

    // Constants for board size (should be accessible from PegBoard)
    private final int BOARD_SIZE = 7; // This should ideally come from PegBoard

    public static void main(String[] args) {
        // This main method is for testing the GUI independently.
        // The actual game launch will be from PegSolitaire.java
        SwingUtilities.invokeLater(() -> {
            PegSolitaire gameInstance = new PegSolitaire(); // Create a dummy game instance
            PegSolitaireGUI gui = new PegSolitaireGUI(gameInstance);
            gui.setVisible(true);
        });
    }
}
