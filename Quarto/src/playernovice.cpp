#include "playernovice.h"

PlayerNovice::PlayerNovice(QString name, Board *board, QObject *parent):
    PlayerRandom(name, board, parent)
{
}

/**
 * @brief PlayerNovice::choosePiece
 * @return
 *
 * Player will choose piece that way, that opponent cannot directly
 * place given piece and immideately win.
 */
Piece *PlayerNovice::choosePiece()
{
    QList<Piece *> stock(board->getStock());

    if (stock.size() <= 0)
        return NULL;

    QList<Piece *>::iterator it = stock.begin();
    while (it != stock.end()) {
        Board possibleBoard(*board);
        possibleBoard.putPiece(PlayerNovice::chooseField(*it), *it);
        if (!possibleBoard.checkVictory())
            ++it;
        else
            it = stock.erase(it);
    }

    QList<Piece *> stockToBePassed = (stock.size() != 0) ? stock : board->getStock();

    return PlayerRandom::choosePieceFromStock(stockToBePassed);
}

/**
 * @brief PlayerNovice::chooseField
 * @param piece
 * @return
 *
 * Choose field from free fields in a way, that if it possible to win
 * due to placing the piece on given field, player will choose that field.
 */
QPair<unsigned, unsigned> PlayerNovice::chooseField(Piece *piece)
{
    QList<QPair<unsigned, unsigned> > freeFields = board->getFreeFields();

    QList<QPair<unsigned, unsigned> >::iterator it = freeFields.begin();
    for (; it != freeFields.end(); it++) {
        Board possibleBoard(*board);
        possibleBoard.putPiece(*it, piece);

        if (possibleBoard.checkVictory()) {
            return *it;
        }
    }
    return PlayerRandom::chooseField(piece);
}
