#ifndef PLAYER_H
#define PLAYER_H

#include <QObject>
#include <QDebug>
#include <QPair>
#include "piece.h"
#include "board.h"
#include "common.h"

class Player : public QObject
{
    Q_OBJECT
public:
    explicit Player(QString name, Board *board, QObject *parent = 0);

    Piece *move(Piece *);
    virtual Piece *choosePiece() = 0;
    QString & getName() { return name; }
    virtual void reset() {}
protected:
    Board *board;
    QString name;

    virtual QPair<unsigned, unsigned> chooseField(Piece *) = 0;

signals:
    
public slots:
    
};

#endif // PLAYER_H
