#include "piece.h"

Piece::Piece(unsigned char properties, QObject *parent) :
    QObject(parent),
    properties(properties)
{
}

/**
 * @brief Piece::Piece serializing support
 * @param pieceStr
 */
Piece::Piece(QString pieceStr)
{
    properties = 0;
    for (int i = 0; i < 4; ++i)  {
        properties |= ((pieceStr[3 - i].toAscii() - '0') << i);
    }
}

/**
 * @brief Piece::toBinaryString serializing support
 * @return
 */
QString Piece::toBinaryString() const
{
    QString result(4);

    for (unsigned i = 0; i < 4; ++i) {
        result[3 - i] = ((properties >> i) & 1) + '0';
    }

    return result;
}

/**
 * @brief Piece::toString serialize piece to string
 * @return
 */
QString Piece::toString() const
{
    QString pieceStr(5, ' ');

//    pieceStr[1] = isSquare() ? '[' : '(';
//    pieceStr[3] = isSquare() ? ']' : ')';

    pieceStr[1] = isSquare() ? '(' : '[';
    pieceStr[3] = isSquare() ? ')' : ']';
    pieceStr[2] = isBlue() ? (isSmall() ? 'b' : 'B') : (isSmall() ? 'r' : 'R');
    if(!isHollow())
        pieceStr.insert(3, '*');

    return pieceStr;
}


