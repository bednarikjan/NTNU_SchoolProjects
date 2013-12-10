#include "playerminimax.h"
#include "board.h"
#include <QStack>
#include <iostream>
#include <cassert>

PlayerMiniMax::PlayerMiniMax(QString name, unsigned maxDepth,
                             Board *board, QObject *parent) :
    PlayerNovice(name, board, parent), maxDepth(maxDepth)
{
    reset();
}

/**
 * @brief PlayerMiniMax::choosePiece
 * @return
 *
 * If it cannot find best piece, just let Novice play, otherwise plays piece
 * saved in best piece.
 */
Piece* PlayerMiniMax::choosePiece()
{
    return (bestPiece == NULL) ? PlayerNovice::choosePiece() : bestPiece;
}

/**
 * Player generates the minimax tree of the depth D while utilizing
 * alpha-beta pruning.
 */
QPair<unsigned, unsigned> PlayerMiniMax::chooseField(Piece *piece)
{            
    if(board->getStock().size() > static_cast<int>(QUARTO_MOVES - NOVICE_MOVES_COUNT) && maxDepth) {
        return PlayerNovice::chooseField(piece);
    }

    // generate search tree
    if(board->getStock().isEmpty()) { /* last free field */
        bestPos = board->getFreeFields().first();
        bestPiece = NULL;
    } else {
        unsigned depth = (maxDepth == 0) ? chooseDepth() : maxDepth;

        struct AlphaBetaResult result = alphabeta(board, piece, depth, MINUS_INF - 1, PLUS_INF + 1, true);
        bestPos = result.field;
        bestPiece = result.piece;
    }

    return bestPos;
}

/**
 * @brief PlayerMiniMax::alphabeta
 * @param board represents state of game
 * @param piece piece to be placed, choosen by opponent
 * @param depth remaining depth
 * @param alpha alpha value
 * @param beta beta value
 * @param maximize max/min
 * @return
 *
 * Classic recursive alfabeta
 */
struct AlphaBetaResult PlayerMiniMax::alphabeta(Board *board, Piece *piece, unsigned depth,
                                                                 int alpha, int beta,
                                                                 bool maximize)
{
    struct AlphaBetaResult bestMove;

    if (board->checkVictory()) { // check victory
        bestMove.score = maximize ? MINUS_INF : PLUS_INF;
        return bestMove;
    }

    if (board->getStock().isEmpty()) { // check last piece
        bestMove.score = (maximize ? -1 : 1) * lastPieceState(board, piece);
        return bestMove;
    }

    if (depth == 0) { // leaf node, evaluate
        bestMove.score = (maximize ? -1 : 1) * evalGameState(board, piece);
        return bestMove;
    }


    QPair<unsigned, unsigned> possibleField;
    Board *possibleBoard = NULL;
    Piece *possiblePiece = NULL;

    foreach (possibleField, board->getFreeFields()) { // for all fields
        possibleBoard = new Board(*board);
        possibleBoard->putPiece(possibleField, piece);

        foreach(possiblePiece, possibleBoard->getStock()) { // for all pieces
            possibleBoard->deletePieceFromStock(possiblePiece);

            if (maximize) {
                struct AlphaBetaResult result = alphabeta(possibleBoard, possiblePiece, depth - 1, alpha, beta, false);
                if (result.score > alpha) {
                    alpha = result.score;
                    bestMove.field = possibleField;
                    bestMove.piece = possiblePiece;
                }

                if (alpha >= beta) { // prune
                    break;
                }
            } else { // minimize
                struct AlphaBetaResult result = alphabeta(possibleBoard, possiblePiece, depth - 1, alpha, beta, true);

                if (result.score < beta) {
                    beta = result.score;
                    bestMove.field = possibleField;
                    bestMove.piece = possiblePiece;
                }

                if (alpha >= beta) { // prune
                    break;
                }
            }

            possibleBoard->addPieceToStock(possiblePiece);
        }

        delete possibleBoard;


        if (alpha >= beta) { // prune
            break;
        }
    }

    bestMove.score = maximize ? alpha : beta;
    return bestMove;
}

/**
 * @brief PlayerMiniMax::lastPieceState
 * @param board
 * @param piece
 * @return
 *
 * If last piece has beem choosen and no more pieces in stock, this function
 * determines if player wins (returns -PLUS_INF) or it is a draw (0).
 */
int PlayerMiniMax::lastPieceState(Board *board, Piece *piece)
{
    QList<QPair<unsigned, unsigned> > freeFields = board->getFreeFields();
    assert(freeFields.size() == 1);
    board->putPiece(freeFields.first(), piece);
    bool victory = board->checkVictory();
    board->deletePiece(freeFields.first());
    return (victory) ?  -PLUS_INF : 0; // loose vs draw
}

/**
 * @brief PlayerMiniMax::chooseDepth
 * @return depth according to how many pieces have been played. It can
 * that calculate many depths in alfabeta is the search space is reduced
 * in later states of game.
 */
int PlayerMiniMax::chooseDepth()
{
    unsigned playedPieces = PIECE_COUNT - board->getStock().size();

    unsigned minimaxLevel = 1;

    switch (playedPieces) {
    case 1:
    case 2:
    case 3:
        minimaxLevel = 2;
        break;
    case 4:
        minimaxLevel = 3;
        break;
    case 5:
    case 6:
        minimaxLevel = 4;
        break;
    case 7:
        minimaxLevel = 5;
        break;
    case 8:
    default:
       minimaxLevel = 8;
       break;
    }
    return minimaxLevel;
}

/**
 * BE SURE THAT YOU CALLED CHECKVICTORY BEFORE!!!
 *
 * @brief PlayerMiniMax::evalGameState
 * @param board
 * @param piece
 * @return
 */
int PlayerMiniMax::evalGameState(Board* board, Piece* piece)
{
    int score = 0;

    // remaining pieces
    score += remainingPiecesScore(board, piece);

    return score;
}

/**
 * @brief PlayerMiniMax::remainingPiecesScore core evaluation function
 * @param board
 * @param piece
 * Evaluates given possition (board + piece) and returns number > 0 if
 * position is good, 0 if draw and < 0 if player thinks that he loose
 */
int PlayerMiniMax::remainingPiecesScore(Board *board, Piece *piece)
{
    static Board *possibleBoard = NULL;
    static unsigned remaining = 0;

    if (!board->hasSameMatrix(possibleBoard)) {
        if (possibleBoard != NULL)
            delete possibleBoard;
        possibleBoard = new Board(*board);

        possibleBoard->addPieceToStock(piece);
        Piece *chosenPiece;

        remaining = possibleBoard->getStock().size();

        foreach (chosenPiece, possibleBoard->getStock()) {
            QPair<unsigned, unsigned> possibleField;

            foreach (possibleField, possibleBoard->getFreeFields()) {
                possibleBoard->putPiece(possibleField, chosenPiece);

                bool victory = possibleBoard->checkVictory();

                possibleBoard->deletePiece(possibleField);

                if (victory) {
                    remaining--;
                    break;
                }
            }
        }
    }

    return (remaining % 2) ? (PLUS_INF - remaining) : (MINUS_INF + remaining);
}

/**
 * @brief PlayerMiniMax::reset resets itself for new game
 */
void  PlayerMiniMax::reset()
{
    bestPiece = NULL;
    bestPos.first = bestPos.second = 0;
}
