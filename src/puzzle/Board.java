package puzzle;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class Board {
    private int[] emptyBlockPos;  // horizontal and vertical positions
    final private int[][] board;

    // construct a board from an n-by-n array of blocks
    // (where blocks[i][j] = block in row i, column j)
    public Board(int[][] blocks) {
        board = new int[blocks.length][blocks.length];
        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks[i].length; j++) {
                if (blocks[i][j] == 0)
                    emptyBlockPos = new int[]{i, j};

                board[i][j] = blocks[i][j];
            }
        }
    }

    // board dimension n
    public int dimension() {
        return board.length;
    }

    // number of blocks out of place
    public int hamming() {
        int diff = 0;
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                int block = board[i][j];
                BlockPosition blockPosition = new BlockPosition(block);
                if (block != 0) {
                    diff += (blockPosition.getRow() != i || blockPosition.getColumn() != j) ? 1 : 0;
                }
            }
        }
        return diff;
    }

    // sum of Manhattan distances between blocks and goal
    public int manhattan() {
        int diff = 0;
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                int block = board[i][j];
                if (block != 0) {
                    BlockPosition blockPosition = new BlockPosition(block);
                    diff += Math.abs(blockPosition.getRow() - i) + Math.abs(blockPosition.getColumn() - j);
                }
            }
        }
        return diff;
    }

    // is this board the goal board?
    public boolean isGoal() {
        int i = 1;
        int[][] s = new int[dimension()][dimension()];
        for (int r = 0; r < s.length; r++) {
            for (int c = 0; c < s[r].length; c++) {
                s[r][c] = i++;
            }
        }
        s[s.length-1][s.length-1] = 0;
        Board search = new Board(s);

        return this.equals(search);
    }

    // a board that is obtained by exchanging any pair of blocks
    public Board twin() {
        int[][] blocks = copyBlocks(board);
        boolean finished = false;

        for (int h = 0; h < blocks.length && !finished; h++) {
            for (int v = 0; v < blocks[h].length; v++) {
                if (v < blocks[h].length - 1 && blocks[h][v] != 0 && blocks[h][v+1] != 0) {
                    swapBlocks(blocks, h, v, h, v+1);
                    finished = true;
                    break;
                }
            }
        }
        return new Board(blocks);
    }

    @Override
    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;

        Board that = (Board) y;

        if (board.length != that.board.length)
            return false;

        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                if (board[row][col] != that.board[row][col])
                    return false;
            }
        }
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        Queue<Board> neighborsBoards = new Queue<>();

        int h = emptyBlockPos[0];  // horizontal
        int v = emptyBlockPos[1];  // vertical

        if (h > 0) {  // top
            int[][] topBlock = copyBlocks(board);
            swapBlocks(topBlock, h - 1, v, h, v);
            neighborsBoards.enqueue(new Board(topBlock));
        }
        if (h < dimension() - 1) {  // bottom
            int[][] bottomBlock = copyBlocks(board);
            swapBlocks(bottomBlock, h, v, h + 1, v);
            neighborsBoards.enqueue(new Board(bottomBlock));
        }
        if (v > 0) {  // left
            int[][] leftBlock = copyBlocks(board);
            swapBlocks(leftBlock, h, v - 1, h, v);
            neighborsBoards.enqueue(new Board(leftBlock));
        }
        if (v < dimension() - 1) {  // right
            int[][] rightBlock = copyBlocks(board);
            swapBlocks(rightBlock, h, v, h, v + 1);
            neighborsBoards.enqueue(new Board(rightBlock));
        }

        return neighborsBoards;
    }

    private int[][] copyBlocks(int[][] original) {
        int[][] blocks = new int[original.length][];
        for (int i = 0; i < original.length; i++)
            blocks[i] = Arrays.copyOf(original[i], original[i].length);

        return blocks;
    }

    private void swapBlocks(int [][] blocks, int h1, int v1, int h2, int v2) {
        int temp = blocks[h1][v1];
        blocks[h1][v1] = blocks[h2][v2];
        blocks[h2][v2] = temp;
    }

    // string representation of this board (in the output format specified below)
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(dimension() + "\n");
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++)
                s.append(String.format("%2d ", board[i][j]));
            s.append("\n");
        }
        return s.toString();
    }

    private class BlockPosition {
        int block;

        BlockPosition(int block) {
            this.block = block;
        }

        int getRow() {
            return (block - 1) / dimension();
        }

        int getColumn() {
            return (block - 1) % dimension();
        }
    }

    // unit tests (not graded)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);
        StdOut.println(initial.toString());
        StdOut.println(initial.dimension());
        StdOut.println(initial.isGoal());
        Board twin = initial.twin();
        StdOut.println(twin);
        StdOut.println(initial);
        StdOut.println("-------------------------");
        for (Board board : initial.neighbors())
            StdOut.println(board);
    }
}
