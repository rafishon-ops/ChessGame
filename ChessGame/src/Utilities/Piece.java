package Utilities;

public class Piece {
	private boolean isWhite;
	private char piece;
	private int rank;
	private int file;
	private boolean hasMoved;
	private String enPassant;

	// Constructs a piece with color, type, and position
	public Piece(boolean isWhite, char piece, int rank, int file) {
		this.isWhite = isWhite;
		this.piece = piece;
		this.rank = rank;
		this.file = file;
		this.hasMoved = false;
		this.enPassant = "Safe";
	}

	// Piece copy constructor
	public Piece(Piece piece) {
		this.isWhite = piece.isWhite;
		this.piece = piece.piece;
		this.rank = piece.rank;
		this.file = piece.file;
		this.hasMoved = piece.hasMoved;
		this.enPassant = piece.enPassant;
	}

	// Getters and Setters
	public char getPiece() {
		return piece;
	}

	public void setPiece(char piece) {
		this.piece = piece;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public int getFile() {
		return file;
	}

	public void setFile(int file) {
		this.file = file;
	}

	public boolean hasMoved() {
		return hasMoved;
	}

	public void move() {
		hasMoved = true;
	}

	public String getEnPassant() {
		return enPassant;
	}

	public void setEnPassant(String enPassant) {
		this.enPassant = enPassant;
	}

	// Promotes a pawn to the given choice
	public void promote(char choice) {
		this.piece = choice;
	}

	private static int min(int a, int b) {
		if (a < b) {
			return a;
		}
		return b;
	}

	private static int max(int a, int b) {
		if (a > b) {
			return a;
		}
		return b;
	}

	// Judges if a piece has access to a given square, provided the position thinks
	// it's that piece's turn
	public boolean hasAccessTo(int newRank, int newFile, Position position) {
		if (notAMove(newRank, newFile)) {
			return false;
		}

		if (position.friendlyOccupied(newRank, newFile)) {
			return false;
		}
		switch (piece) {
		case 'K':
			return (kingAccess(newRank, newFile));
		case 'Q':
			return (queenAccess(newRank, newFile, position));
		case 'R':
			return (rookAccess(newRank, newFile, position));
		case 'N':
			return (knightAccess(newRank, newFile));
		case 'B':
			return (bishopAccess(newRank, newFile, position));
		default:
			if (isWhite) {
				return (whitePawnAccess(newRank, newFile, position));
			}
			return (blackPawnAccess(newRank, newFile, position));
		}

	}

	// Judges if a piece is trying to move to its own square
	public boolean notAMove(int newRank, int newFile) {
		if (rank == newRank && file == newFile) {
			return true;
		}
		return false;
	}

	// Judges if a king has access to a given square
	public boolean kingAccess(int newRank, int newFile) {
		if (Math.abs(newRank - rank) > 1 || Math.abs(newFile - file) > 1) {
			return false;
		}
		return true;
	}

	// Judges if a knight has access to a given square
	public boolean knightAccess(int newRank, int newFile) {
		if (Math.abs(newRank - rank) == 1 && Math.abs(newFile - file) == 2) {
			return true;
		}
		if (Math.abs(newRank - rank) == 2 && Math.abs(newFile - file) == 1) {
			return true;
		}
		return false;
	}

	// Judges is a queen has access to a given square
	public boolean queenAccess(int newRank, int newFile, Position position) {
		return (rookAccess(newRank, newFile, position) || bishopAccess(newRank, newFile, position));
	}

	// Judges if a rook has access to a given square
	public boolean rookAccess(int newRank, int newFile, Position position) {
		if (rank == newRank) {
			for (int i = min(file, newFile) + 1; i != max(file, newFile); i++) {
				if (position.occupied(rank, i)) {
					return false;
				}
			}
			return true;
		}
		if (file == newFile) {
			for (int i = min(rank, newRank) + 1; i != max(rank, newRank); i++) {
				if (position.occupied(i, file)) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	// Judges if a bishop has access to a given square
	public boolean bishopAccess(int newRank, int newFile, Position position) {
		if (rank - newRank == file - newFile) {
			for (int i = 1; i != Math.abs(rank - newRank); i++) {
				if (position.occupied(min(rank, newRank) + i, min(file, newFile) + i)) {
					return false;
				}
			}
			return true;
		}
		if (rank - newRank == newFile - file) {
			for (int i = 1; i != Math.abs(rank - newRank); i++) {
				if (position.occupied(min(rank, newRank) + i, max(newFile, file) - i)) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	// Judges if a white pawn has access to a given square, provided the position
	// thinks it's white's turn
	public boolean whitePawnAccess(int newRank, int newFile, Position position) {
		if (file == newFile) {
			if ((rank + 1 == newRank || (rank == 2 && newRank == 4 && !position.occupied(3, file)))
					&& !position.occupied(newRank, newFile)) {
				return true;
			}
		}
		if (Math.abs(file - newFile) == 1) {
			if (rank + 1 == newRank && position.enemyOccupied(newRank, newFile)) {
				return true;
			}
		}
		return false;
	}

	// Judges if a black pawn has access to a given square, provided the position
	// thinks it's black's turn
	public boolean blackPawnAccess(int newRank, int newFile, Position position) {
		if (file == newFile) {
			if ((rank - 1 == newRank || (rank == 7 && newRank == 5 && !position.occupied(6, file)))
					&& !position.occupied(newRank, newFile)) {
				return true;
			}
		}
		if (Math.abs(file - newFile) == 1) {
			if (rank - 1 == newRank && position.enemyOccupied(newRank, newFile)) {
				return true;
			}
		}
		return false;
	}

}
