#ifndef PIECE_H
#define PIECE_H

#include <QObject>
#include <QDebug>
#include <QString>

const unsigned char PIECE_SMALL     = 4;
const unsigned char PIECE_BLUE      = 8;
const unsigned char PIECE_HOLLOW    = 2;
const unsigned char PIECE_SQUARE    = 1;

class Piece : public QObject
{
    Q_OBJECT
public:
    explicit Piece(unsigned char properties, QObject *parent = 0);
    Piece(QString pieceStr);
    
    bool isSmall()  const { return properties & PIECE_SMALL; }
    bool isBlue()   const { return properties & PIECE_BLUE; }
    bool isHollow() const { return properties & PIECE_HOLLOW; }
    bool isSquare() const { return properties & PIECE_SQUARE; }

    QString toBinaryString() const;

    QString toString() const;

    unsigned getProperties() { return properties; }
private:
    unsigned char properties;
    void print();

signals:
    
public slots:
    
};

#endif // PIECE_H
