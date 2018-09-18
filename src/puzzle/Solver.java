package puzzle;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;
import java.util.Stack;
import java.util.Collections;


public class Solver {
    private Node current;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board board) {
        if (board == null)
            throw new IllegalArgumentException("initial bord can't be empty!");

        MinPQ<Node> boardMinPQ = new MinPQ<>();
        MinPQ<Node> twinMinPQ = new MinPQ<>();

        int i = 1;
        int[][] s = new int[board.dimension()][board.dimension()];
        for (int r = 0; r < s.length; r++) {
            for (int c = 0; c < s[r].length; c++) {
                s[r][c] = i++;
            }
        }
        s[s.length-1][s.length-1] = 0;

        Board search = new Board(s);

        current = new Node(board, null);
        boardMinPQ.insert(current);

        Node twin = new Node(board.twin(), null);
        twinMinPQ.insert(twin);

        while (!current.board.equals(search) && !twin.board.equals(search)) {
            for (Board b : current.board.neighbors()) {
                if (current.moves > 0) {
                    Node node = new Node(b, current);
                    boardMinPQ.insert(node);
                } else {
                    Node node = new Node(b, current);
                    boardMinPQ.insert(node);
                }
            }
            current = boardMinPQ.delMin();

            for (Board b : twin.board.neighbors()) {
                if (twin.moves > 0) {
                    Node node = new Node(b, twin);
                    twinMinPQ.insert(node);
                } else {
                    Node node = new Node(b, twin);
                    twinMinPQ.insert(node);
                }
            }
            twin = twinMinPQ.delMin();
        }
}

    // is the initial board solvable?
    public boolean isSolvable() {
        return current.manhattan == 0;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (!isSolvable()) return -1;
        return current.moves;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!isSolvable()) return null;
        Stack<Board> boards = new Stack<>();

        Node current = new Node(this.current.board, this.current.previous);
        while (current != null) {
            boards.push(current.board);
            current = current.previous;
        }
        Collections.reverse(boards);
        return boards;
    }

    private class Node implements Comparable<Node> {
        Board board;
        Node previous;
        int manhattan;
        int moves;
        int priority;


        Node (Board initial, Node _previous) {
            board = initial;
            previous = null;
            this.previous = _previous;
            this.moves = (previous == null) ? 0 : previous.moves + 1;
            this.manhattan = board.manhattan();
            priority = manhattan + priority;
        }

        @Override
        public int compareTo(Node that) {
            return (this.priority - that.priority);
        }
    }

    // solve a slider puzzle (given below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            int i = 0;
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution()) {
                i++;
                StdOut.println(board);
            }
            StdOut.println("Solution length = " + i);
            StdOut.println("Solution moves = " + solver.moves());
        }
    }
}
