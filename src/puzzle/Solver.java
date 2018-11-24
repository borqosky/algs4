package puzzle;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.Stack;
import java.util.Collections;


public class Solver { private Node current;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board board) {
        if (board == null)
            throw new IllegalArgumentException("initial bord can't be empty!");

        MinPQ<Node> boardMinPQ = new MinPQ<>();
        MinPQ<Node> twinMinPQ = new MinPQ<>();

        boardMinPQ.insert(new Node(board, null));
        twinMinPQ.insert(new Node(board.twin(), null));

        while (!boardMinPQ.min().board.isGoal() && !twinMinPQ.min().board.isGoal()) {
            current = boardMinPQ.delMin();
            Node twin = twinMinPQ.delMin();

            for (Board b : current.board.neighbors()) {
                if (current.moves > 0 && !b.equals(current.previous.board))
                    boardMinPQ.insert(new Node(b, current));
                else if (current.moves == 0)
                    boardMinPQ.insert(new Node(b, current));
            }

            for (Board b : twin.board.neighbors()) {
                if (twin.moves > 0 && !b.equals(twin.previous.board))
                    twinMinPQ.insert(new Node(b, twin));
                else if (current.moves == 0)
                    twinMinPQ.insert(new Node(b, twin));
            }
        }
        current = boardMinPQ.delMin();
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

        Node temp = new Node(this.current.board, this.current.previous);
        while (temp != null) {
            boards.push(temp.board);
            temp = temp.previous;
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

        Node (Board initial, Node prev) {
            board = initial;
            previous = null;
            this.previous = prev;
            this.moves = (previous == null) ? 0 : previous.moves + 1;
            this.manhattan = board.manhattan();
            priority = manhattan + moves;
        }

        @Override
        public int compareTo(Node that) {
            return this.priority - that.priority;
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
