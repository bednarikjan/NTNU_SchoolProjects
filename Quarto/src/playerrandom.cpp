#include "playerrandom.h"

#include <time.h>

#ifdef __unix__
    #include <unistd.h>
#endif

PlayerRandom::PlayerRandom(QString name, Board *board, QObject *parent) :
    Player(name, board, parent)
{
#ifdef __unix__
    srand(time(NULL) * getpid());
#else
    srand(time(NULL));
#endif
}

/**
 * @brief PlayerRandom::choosePieceFromStock Player randomly choose piece from given stock
 * @param stock
 * @return piece if stock is not empty, NULL otherwise
 */
Piece *PlayerRandom::choosePieceFromStock(const QList<Piece *> & stock)
{
    if (stock.size() <= 0)
        return NULL;

    unsigned indexChoosen = rand() % stock.size();
    return stock[indexChoosen];
}

/**
 * @brief PlayerRandom::choosePiece Player randomly choose piece from board stock
 * @return piece if stock is not empty, NULL otherwise
 */
Piece *PlayerRandom::choosePiece()
{
    return choosePieceFromStock(board->getStock());
}

/**
 * @brief PlayerRandom::chooseField randomly choose field
 * @return
 */
QPair<unsigned, unsigned> PlayerRandom::chooseField(Piece *)
{
    QList<QPair<unsigned, unsigned> > freeFields = board->getFreeFields();
    return freeFields[rand() % freeFields.size()];
}
