package Utilities;

import java.util.ArrayList;

public class Position {
	private Piece[][] pieces;
	private char[][] board;
	private Integer turn;
	public static ArrayList<String> positionList = new ArrayList<>();

	/*
	 * Constructs a Position with a 2d array to store pieces, a 2d array to store
	 * what's on each square, and white's turn
	 */
	public Position() {
		pieces = new Piece[2][16];
		board = new char[8][8];
		turn = 0;
	}

	// Position deep copy constructor
	public Position(Position position) {
		pieces = new Piece[2][16];
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 16; j++) {
				if (position.pieces[i][j] == null) {
					this.pieces[i][j] = null;
				} else {
					this.pieces[i][j] = new Piece(position.pieces[i][j]);
				}
			}
		}

		board = new char[8][8];
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				this.board[i][j] = position.board[i][j];
			}
		}

		this.turn = position.turn;
	}

	// Getters & Setters
	public Piece[][] getPieces() {
		return pieces;
	}

	public char[][] getBoard() {
		return board;
	}

	public void setPosition(int rank, int file, char piece) {
		board[rank][file] = piece;
	}

	public int getTurn() {
		return turn;
	}

	public void changeTurn() {
		turn = (turn == 0 ? 1 : 0);
	}

	// Sets up with the starting position
	public void setUp() {
		pieces[0][0] = new Piece(true, 'P', 2, 1);
		pieces[0][1] = new Piece(true, 'P', 2, 2);
		pieces[0][2] = new Piece(true, 'P', 2, 3);
		pieces[0][3] = new Piece(true, 'P', 2, 4);
		pieces[0][4] = new Piece(true, 'P', 2, 5);
		pieces[0][5] = new Piece(true, 'P', 2, 6);
		pieces[0][6] = new Piece(true, 'P', 2, 7);
		pieces[0][7] = new Piece(true, 'P', 2, 8);
		pieces[0][8] = new Piece(true, 'R', 1, 1);
		pieces[0][9] = new Piece(true, 'R', 1, 8);
		pieces[0][10] = new Piece(true, 'N', 1, 2);
		pieces[0][11] = new Piece(true, 'N', 1, 7);
		pieces[0][12] = new Piece(true, 'B', 1, 3);
		pieces[0][13] = new Piece(true, 'B', 1, 6);
		pieces[0][14] = new Piece(true, 'Q', 1, 4);
		pieces[0][15] = new Piece(true, 'K', 1, 5);
		pieces[1][0] = new Piece(false, 'P', 7, 1);
		pieces[1][1] = new Piece(false, 'P', 7, 2);
		pieces[1][2] = new Piece(false, 'P', 7, 3);
		pieces[1][3] = new Piece(false, 'P', 7, 4);
		pieces[1][4] = new Piece(false, 'P', 7, 5);
		pieces[1][5] = new Piece(false, 'P', 7, 6);
		pieces[1][6] = new Piece(false, 'P', 7, 7);
		pieces[1][7] = new Piece(false, 'P', 7, 8);
		pieces[1][8] = new Piece(false, 'R', 8, 1);
		pieces[1][9] = new Piece(false, 'R', 8, 8);
		pieces[1][10] = new Piece(false, 'N', 8, 2);
		pieces[1][11] = new Piece(false, 'N', 8, 7);
		pieces[1][12] = new Piece(false, 'B', 8, 3);
		pieces[1][13] = new Piece(false, 'B', 8, 6);
		pieces[1][14] = new Piece(false, 'Q', 8, 4);
		pieces[1][15] = new Piece(false, 'K', 8, 5);
		for (int i = 2; i < 6; i++) {
			for (int j = 0; j < 8; j++) {
				board[i][j] = ' ';
			}
		}
		for (int i = 0; i < 8; i++) {
			board[1][i] = 'P';
			board[6][i] = 'p';
		}
		board[0][0] = board[0][7] = 'R';
		board[0][1] = board[0][6] = 'N';
		board[0][2] = board[0][5] = 'B';
		board[0][3] = 'Q';
		board[0][4] = 'K';
		board[7][0] = board[7][7] = 'r';
		board[7][1] = board[7][6] = 'n';
		board[7][2] = board[7][5] = 'b';
		board[7][3] = 'q';
		board[7][4] = 'k';

		positionList.add(getPositionString());
	}

	// Returns a string representation of a position
	public String getPositionString() {
		String position = "";
		for (int i = 7; i > -1; i--) {
			for (int j = 0; j < 8; j++) {
				position += board[i][j];
			}
			position += '\n';
		}
		return position;
	}
	
	public void printPosition() {
		String result = "";
		for (int i = 7; i > -1; i--) {
			for (int j = 0; j < 8; j++) {
				result += '|';
				result += getBoard()[i][j];
			}
			result += "|\n";
		}
		System.out.println(result);
	}

	/*
	 * Moves the piece at the given spot in the pieces array to the given square,
	 * captures an enemy piece which is there, and promotes if applicable
	 */
	public void move(int turn, int index, int rank, int file, String promotion) {
		board[rank - 1][file - 1] = board[pieces[turn][index].getRank() - 1][pieces[turn][index].getFile() - 1];
		board[pieces[turn][index].getRank() - 1][pieces[turn][index].getFile() - 1] = ' ';

		pieces[turn][index].setRank(rank);
		pieces[turn][index].setFile(file);
		pieces[turn][index].move();
		if (promotion != null) {
			pieces[turn][index].setPiece(promotion.charAt(0));
			setPosition(rank - 1, file - 1, (turn == 0 ? promotion : promotion.toLowerCase()).charAt(0));
		}

		for (int i = 0; i < 16; i++) {
			if (pieces[turn == 0 ? 1 : 0][i] != null && pieces[turn == 0 ? 1 : 0][i].getRank() == rank
					&& pieces[turn == 0 ? 1 : 0][i].getFile() == file) {
				pieces[turn == 0 ? 1 : 0][i] = null;
				break;
			}
		}
	}

	/*
	 * Makes the final, legal move; saves a new en passant target if necessary, adds
	 * the new position to the list of positions
	 */
	public void executeMove(int turn, int index, int rank, int file, String promotion) {
		if (index < 8 && Math.abs(rank - pieces[turn][index].getRank()) == 2) {
			pieces[turn][index].setEnPassant("Target Acquired");
		}
		move(turn, index, rank, file, promotion);
		positionList.add(getPositionString());
	}

	// Finds out if a given square is occupied
	public boolean occupied(int rank, int file) {
		if (board[rank - 1][file - 1] == ' ') {
			return false;
		}
		return true;
	}

	// Finds out if a given square is occupied by a piece whose turn it is
	public boolean friendlyOccupied(int rank, int file) {
		int occupier = (int) board[rank - 1][file - 1];
		if (turn == 0) {
			if (occupier >= 65 && occupier <= 90) {
				return true;
			}
		}
		if (turn == 1) {
			if (occupier >= 97 && occupier <= 122) {
				return true;
			}
		}
		return false;
	}

	// Finds out if a given square is occupied by a piece whose turn it isn't
	public boolean enemyOccupied(int rank, int file) {
		return (occupied(rank, file) && !friendlyOccupied(rank, file));
	}

	// Finds out if whoever's turn the position says it is is putting the opponent
	// in check (Practically, the turn has usually been updated at this point)
	public boolean isInCheck() {
		for (Piece attacker : this.pieces[turn]) {
			if (attacker != null && attacker.hasAccessTo(this.pieces[turn == 0 ? 1 : 0][15].getRank(),
					this.pieces[turn == 0 ? 1 : 0][15].getFile(), this)) {
				return true;
			}
		}
		return false;
	}

	// Finds out if whoever's turn the position says it is is in check
	// (Practically, the turn has usually been updated at this point)
	public boolean check() {
		changeTurn();
		boolean check = isInCheck();
		changeTurn();
		return check;
	}

	// Returns true if whoever's turn the position says it is has no legal moves
	public boolean noLegalMoves() {
		for (int i = 0; i < 16; i++) {
			for (int j = 1; j <= 8; j++) {
				for (int k = 1; k <= 8; k++) {
					if (pieces[turn][i] != null && pieces[turn][i].hasAccessTo(j, k, this)) {
						Position trial = new Position(this);
						trial.move(turn, i, j, k, null);
						if (trial.check()) {
							continue;
						}
						return false;
					}
				}
			}
		}
		return true;
	}

	// Returns true if whoever's turn the position says it is is in checkmate
	public boolean checkmate() {
		if (check() && noLegalMoves()) {
			return true;
		}
		return false;
	}

	// Returns true if whoever's turn the position says it is is in stalemate
	public boolean stalemate() {
		if (!check() && noLegalMoves()) {
			return true;
		}
		return false;
	}

	// Finds out if it's a draw by repetition
	public static boolean repetition() {
		for (int i = (positionList.size() + 1) % 2; i < positionList.size(); i += 2) {
			for (int j = i + 2; j < positionList.size(); j += 2) {
				if (positionList.get(j).equals(positionList.get(i))) {
					for (int k = j + 2; k < positionList.size(); k += 2) {
						if (positionList.get(k).equals(positionList.get(j))) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	// Finds out if it's a draw by insufficient material
	public boolean insufficientMaterial() {
		String pieces = getPositionString().replaceAll(" ", "").replaceAll("K", "").replaceAll("k", "").replaceAll("\n",
				"");
		if (pieces.length() > 1) {
			return false;
		}
		if (pieces.length() == 0) {
			return true;
		}
		if (pieces.charAt(0) == 'N' || pieces.charAt(0) == 'n' || pieces.charAt(0) == 'B' || pieces.charAt(0) == 'b') {
			return true;
		}
		return false;
	}

}
