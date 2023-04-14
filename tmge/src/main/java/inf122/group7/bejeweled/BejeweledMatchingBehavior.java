package inf122.group7.bejeweled;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import inf122.group7.model.EmptyTile;
import inf122.group7.model.GameBoard;
import inf122.group7.model.IMatchBehavior;
import inf122.group7.model.Tile;

import java.awt.Point;

public class BejeweledMatchingBehavior implements IMatchBehavior {
    private static final int MATCH_MINIMUM = 3;

    public List<Point> getMatches(GameBoard board)
    {
        Set<Point> matchingGemLocations = new HashSet<>();

        // Check all horizontal matches
        for(int row = 0; row < board.getHeight(); row++)
        {
            Tile streakGem = new EmptyTile();
            int gemsInStreak = 1;

            // Loop through each gem in the current row
            for(int col = 0; col < board.getWidth(); col++)
            {
                Tile currentGem = board.getTile(new Point(col, row));

                // If the current gem matches the previous gem, add one to the current streak
                if (currentGem.matches(streakGem))
                {
                    gemsInStreak += 1;
                }
                // Otherwise, evaluate whether the previous gems were a matching stream
                else
                {
                    // If the previous gems had 3 or more matching in a row, add all of their locations
                    if(gemsInStreak >= MATCH_MINIMUM)
                    {
                        for(int i = 0; i < gemsInStreak; i++)
                        {
                            matchingGemLocations.add(new Point(col - i - 1, row));
                        }
                    }

                    gemsInStreak = 1;
                    streakGem = currentGem;
                }
            }

            // If the row ended with a streak, add all of their locations
            if(gemsInStreak >= MATCH_MINIMUM)
            {
                for(int i = 0; i < gemsInStreak; i++)
                {
                    matchingGemLocations.add(new Point(board.getWidth() - 1 - i, row));
                }
            }
        }

        // Check all vertical matches
        for(int col = 0; col < board.getHeight(); col++)
        {
            Tile streakGem = new EmptyTile();
            int gemsInStreak = 1;

            // Loop through each gem in the current column
            for(int row = 0; row < board.getWidth(); row++)
            {
                Tile currentGem = board.getTile(new Point(col, row));

                // If the current gem matches the previous gem, add one to the current streak
                if (currentGem.matches(streakGem))
                {
                    gemsInStreak += 1;
                }
                // Otherwise, evaluate whether the previous gems were a matching stream
                else
                {
                    // If the previous gems had 3 or more matching in a row, add all of their locations
                    if(gemsInStreak >= MATCH_MINIMUM)
                    {
                        for(int i = 0; i < gemsInStreak; i++)
                        {
                            matchingGemLocations.add(new Point(col, row - i - 1));
                        }
                    }

                    gemsInStreak = 1;
                    streakGem = currentGem;
                }
            }

            // If the column ended with a streak, add all of their locations
            if(gemsInStreak >= MATCH_MINIMUM)
            {
                for(int i = 0; i < gemsInStreak; i++)
                {
                    matchingGemLocations.add(new Point(col, board.getHeight() - 1 - i));
                }
            }
        }

        // support Java11
        return matchingGemLocations.stream().collect(Collectors.toList());
    }

    public boolean hasPossibleMoves(GameBoard board)
    {
        // Check all horizontal matches
        for(int row = 0; row < board.getHeight(); row++)
        {
            Tile streakGem = new EmptyTile();
            int gemsInStreak = 1;

            // Loop through each gem in the current row
            for(int col = 0; col < board.getWidth(); col++)
            {
                Tile currentGem = board.getTile(new Point(col, row));

                // If the current gem matches the previous gem, add one to the current streak
                if (currentGem.matches(streakGem))
                {
                    gemsInStreak += 1;
                }
                // Otherwise, evaluate whether the previous gems were a matching stream
                else
                {
                    // If the previous gems had 2 or more matching in a row, see if there's a possible move before or after the streak
                    if(gemsInStreak == MATCH_MINIMUM - 1)
                    {
                        // Check if any gem can be moved to the current position to form the streak
                        boolean foundMove = hasAdjacentMatchingGem(board, row, col, streakGem, Direction.WEST);

                        // If not, check if any gem can be moved from the other side to form the streak
                        if(!foundMove && col - MATCH_MINIMUM >= 0)
                        {
                            foundMove = hasAdjacentMatchingGem(board, row, col - MATCH_MINIMUM, streakGem, Direction.EAST);
                        }

                        if(foundMove)
                        {
                            return true;
                        }
                    }

                    gemsInStreak = 1;
                    streakGem = currentGem;
                }
            }

            // If the previous gems had 2 or more matching in a row, see if there's a possible move before the streak
            if(gemsInStreak == MATCH_MINIMUM - 1)
            {
                if(hasAdjacentMatchingGem(board, row, board.getWidth() - MATCH_MINIMUM, streakGem, Direction.EAST))
                {
                    return true;
                }
            }
        }

        // Check all vertical matches
        for(int col = 0; col < board.getWidth(); col++)
        {
            Tile streakGem = new EmptyTile();
            int gemsInStreak = 1;

            // Loop through each gem in the current column
            for(int row = 0; row < board.getHeight(); row++)
            {
                Tile currentGem = board.getTile(new Point(col, row));

                // If the current gem matches the previous gem, add one to the current streak
                if (currentGem.matches(streakGem))
                {
                    gemsInStreak += 1;
                }
                // Otherwise, evaluate whether the previous gems were a matching stream
                else
                {
                    // If the previous gems had 2 or more matching in a row, see if there's a possible move before or after the streak
                    if(gemsInStreak == MATCH_MINIMUM - 1)
                    {
                        // Check if any gem can be moved to the current position to form the streak
                        boolean foundMove = hasAdjacentMatchingGem(board, row, col, streakGem, Direction.NORTH);

                        // If not, check if any gem can be moved from the other side to form the streak
                        if(!foundMove && row - MATCH_MINIMUM >= 0)
                        {
                            foundMove = hasAdjacentMatchingGem(board, row - MATCH_MINIMUM, col, streakGem, Direction.SOUTH);
                        }

                        if(foundMove)
                        {
                            return true;
                        }
                    }

                    gemsInStreak = 1;
                    streakGem = currentGem;
                }
            }

            // If the previous gems had 2 or more matching in a row, see if there's a possible move before the streak
            if(gemsInStreak == MATCH_MINIMUM - 1)
            {
                if(hasAdjacentMatchingGem(board, board.getHeight() - MATCH_MINIMUM, col, streakGem, Direction.SOUTH))
                {
                    return true;
                }
            }
        }

        return false;
    }

    private enum Direction {
        NORTH,
        SOUTH,
        EAST,
        WEST
    }

    private static boolean hasAdjacentMatchingGem(GameBoard board, int row, int col, Tile gem, Direction ignoredDirection) {
        boolean foundMatchingGem = false;

        if(ignoredDirection != Direction.NORTH && row - 1 >= 0)
        {
            foundMatchingGem = board.getTile(new Point(col, row - 1)).matches(gem);
        }

        if(ignoredDirection != Direction.SOUTH && row + 1 < board.getHeight())
        {
            foundMatchingGem = board.getTile(new Point(col, row + 1)).matches(gem);
        }

        if(ignoredDirection != Direction.EAST && col + 1 < board.getWidth())
        {
            foundMatchingGem = board.getTile(new Point(col + 1, row)).matches(gem);
        }

        if(ignoredDirection != Direction.WEST && col - 1 >= 0)
        {
            foundMatchingGem = board.getTile(new Point(col - 1, row)).matches(gem);
        }

        return foundMatchingGem;
    }
}
