#include "playerhuman.h"
#include <iostream>
#include <QTextStream>
#include <cassert>

PlayerHuman::PlayerHuman(QString name, Board *board, QObject *parent) :
    Player(name, board, parent)
{    
}

/**
 * @brief PlayerHuman::choosePiece reads piece choises from standard input
 * @return
 */
Piece* PlayerHuman::choosePiece()
{
    bool correctChoice = false;
    bool ok;
    int choice;
    QTextStream streamIn(stdin);
    QString input;

    board->printMatrix();
    board->printStock();

    do {
        std::cout << "Choose piece: ";
        input = streamIn.readLine();
        if(input.isNull())
            continue;

        choice = input.toInt(&ok, 10);
        if (!ok || choice < 0 || choice >= board->getStock().size())
            continue;

        correctChoice = true;
    } while (!correctChoice);

    return board->getStock()[choice];
}

/**
 * @brief PlayerHuman::getCoordToPutPiece writes and reads prompt for reading
 * row/column input
 * @param row
 */
unsigned PlayerHuman::getCoordToPutPiece(bool row)
{
    bool correctChoice = false;
    bool ok;
    unsigned coord;

    QTextStream streamIn(stdin);
    QString input;

    do {
        std::cout << "Choose " << ((row) ? "row" : "column") << " where to put piece (" << ((row) ? "1-4" : "W-Z") << "): ";
        input = streamIn.readLine();
        if (input.isNull() || input.size() == 0)
            continue;

        coord = (row) ? input.toInt(&ok, 10) - 1 : input.at(0).toAscii() - 'W';

        if (!ok || coord >= (unsigned)MATRIX_SIZE)
            continue;

        correctChoice = true;
    } while (!correctChoice);

    return coord;
}

/**
 * @brief PlayerHuman::chooseField from standard input
 * @param piece
 * @return
 */
QPair<unsigned, unsigned> PlayerHuman::chooseField(Piece *)
{
    unsigned row;
    unsigned col;

    board->printMatrix();
    board->printStock();

    do {
        row = getCoordToPutPiece(true);
        col = getCoordToPutPiece(false);

        assert(col < MATRIX_SIZE && row < MATRIX_SIZE);

        if (board->getMatrix()[row][col])
            std::cout << "That position is occupied! Try again!\n";
    } while (board->getMatrix()[row][col]);

    return QPair<unsigned, unsigned> (row, col);
}
