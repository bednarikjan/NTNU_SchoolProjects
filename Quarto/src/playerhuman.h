#ifndef PLAYERHUMAN_H
#define PLAYERHUMAN_H

#include "player.h"

class PlayerHuman : public Player
{
public:
    PlayerHuman(QString name, Board *board, QObject *parent = 0);
    Piece *choosePiece();

private:
    QPair<unsigned, unsigned> chooseField(Piece *piece);
    unsigned getCoordToPutPiece(bool row);
};

#endif // PLAYERHUMAN_H
