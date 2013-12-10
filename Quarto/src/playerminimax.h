#ifndef PLAYERMINIMAX_H
#define PLAYERMINIMAX_H

#include "playernovice.h"
#include "piece.h"
#include "QPair"

struct AlphaBetaResult
{
    int score;
    QPair <unsigned, unsigned> field;
    Piece *piece;
};


class PlayerMiniMax : public PlayerNovice
{
public:
    /** constructor */
    PlayerMiniMax(QString name, unsigned maxDepth, Board *board, QObject *parent);
    Piece *choosePiece();

private:
    // TODO - cachovani stromu
    // promenna ukazujici na koren stromu minimax

    unsigned maxDepth; /**< max depth */
    QPair<unsigned, unsigned> bestPos;
    Piece *bestPiece;

    QPair<unsigned, unsigned> chooseField(Piece *piece);    
//    int alphabeta(Board* board, Piece* piece, unsigned D, int alpha, int beta, bool maximize);

    AlphaBetaResult alphabeta(Board *board,
                                                                     Piece *,
                                                                     unsigned depth,
                                                                     int alpha, int beta,
                                                                     bool maximize);
    int evalGameState(Board* board, Piece* piece);

    int remainingPiecesScore(Board *board, Piece *piece);

    int lastPieceState(Board *board, Piece *piece);
    int chooseDepth();
    void  reset();
};

#endif // PLAYERMINIMAX_H
