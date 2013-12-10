#ifndef PLAYERNOVICE_H
#define PLAYERNOVICE_H

#include "playerrandom.h"

class PlayerNovice : public PlayerRandom
{
public:
    PlayerNovice(QString name, Board *board, QObject *parent = 0);
    Piece *choosePiece();

protected:
    QPair<unsigned, unsigned> chooseField(Piece *piece);
};

#endif // PLAYERNOVICE_H
