#include <iostream>
#include <QTextStream>
#include <QString>
#include "board.h"
#include "common.h"

Board::Board(QObject *parent) : QObject(parent)
{
    reset();
}

/**
 * Copy constructor
 */
Board::Board(Board & other, QObject *parent) : QObject(parent)
{
    stock = other.getStock();

    // allocate matrix
    matrix = new Piece**[MATRIX_SIZE];
    for (unsigned i = 0; i < MATRIX_SIZE; i++) {
        matrix[i] = new Piece*[MATRIX_SIZE];
        for (unsigned j = 0; j < MATRIX_SIZE; j++) {
            matrix[i][j] = other.getMatrix()[i][j];
        }
    }
}

Board::~Board()
{
    // deallocate the whole matrix
    for (unsigned i = 0; i < MATRIX_SIZE; ++i)
        delete[] matrix[i];
    delete[] matrix;
}

/**
 * @brief Prints current board state with coordinates.
 */
void Board::printMatrix()
{
    QTextStream cout(stdout);

    cout << endl;
    cout << "     W     X     Y     Z   " << endl;
    cout << "  -------------------------" << endl;

    for(unsigned int r = 0; r < MATRIX_SIZE; r++) {
        cout << r + 1 << " |";
        for(unsigned int c = 0; c < MATRIX_SIZE; c++) {
            cout << qSetFieldWidth(6) << left;
            cout << piece2str(matrix[r][c]);
        }
        cout << qSetFieldWidth(0) << endl;
        cout << "  |" << endl;
    }
}

/**
 * @brief Prints current stock with options.
 */
void Board::printStock()
{
    QTextStream cout(stdout);
    cout << endl;

    for(int i = 0; i < stock.size(); i++) {
        cout << i << ':';
        cout << qSetFieldWidth(6) << center;
        cout << piece2str(stock[i]);
        cout << qSetFieldWidth(0);
    }
    cout << endl << endl;
}

/**
 * @brief Board::getFreeSpots finds all free spots in matrix
 * @return pairs of indexes to matrix
 */
QList<QPair<unsigned, unsigned> > Board::getFreeFields()
{
    QList<QPair<unsigned, unsigned> > retval;
    for (unsigned i = 0; i < MATRIX_SIZE; ++i)
        for (unsigned j = 0; j < MATRIX_SIZE; ++j)
            if (matrix[i][j] == NULL)
                retval.append(QPair<unsigned, unsigned> (i,j));
    return retval;
}

/**
 * @brief Creates textual representation of the piece.
 * @param piece Piece pointer
 * @return string representing piece's propeties
 */
QString Board::piece2str(Piece *piece)
{
    return (piece == NULL) ? "  .  " : piece->toString();
}

/**
 * @brief Board::checkVictory check if there isnt placed winning combination of
 * pieces.
 * @return
 * @IDEA: precalculate coordinates, eg 11,22,33,44 for diagonal instead duing it in iteration
 *        or unwrap iterations by gcc
 */
bool Board::checkVictory()
{
    QList<Piece *> fields;

    // check diagonals
    fields.append(matrix[0][0]);
    for (unsigned i = 0; i < MATRIX_SIZE; ++i)
        fields.append(matrix[i][i]);

    if (checkVictoryFields(fields))
        return true;

    fields.clear();

    for (unsigned i = 0; i < MATRIX_SIZE; ++i)
        fields.append(matrix[i][MATRIX_SIZE - 1 - i]);

    if (checkVictoryFields(fields))
        return true;

    // check top-down
    for (unsigned i = 0; i < MATRIX_SIZE; ++i) {
        fields.clear();
        for (unsigned j = 0; j < MATRIX_SIZE; ++j)
            fields.append(matrix[i][j]);

        if (checkVictoryFields(fields))
            return true;
    }

    // check left-right
    for (unsigned i = 0; i < MATRIX_SIZE; ++i) {
        fields.clear();
        for (unsigned j = 0; j < MATRIX_SIZE; ++j)
            fields.append(matrix[j][i]);

        if (checkVictoryFields(fields))
            return true;
    }
    return false;
}

/**
 * @brief Board::putPiece put piece on given field
 * @param index
 * @param piece
 */
void Board::putPiece(const QPair<unsigned, unsigned> & index, Piece *piece)
{
    if (index.first >= MATRIX_SIZE || index.second >= MATRIX_SIZE)
        qDebug() << "VALGRIND: " << index.first << index.second << piece;
    if (matrix[index.first][index.second] != NULL) {
        qDebug() << "Trying to put" << piece->toString() << "on " << index.first + 1 << QChar(index.second + 'W');
        throw "Cannot put piece on piece";
    }
    matrix[index.first][index.second] = piece;
    lastMove = index;
}

/**
 * @brief Board::checkVictoryFields check if all given fields share a property
 * @param fields
 * @return
 */
bool Board::checkVictoryFields(const QList<Piece *> & fields)
{
    unsigned properties = PIECE_BLUE | PIECE_HOLLOW | PIECE_SMALL | PIECE_SQUARE;
    unsigned properties_invert = PIECE_BLUE | PIECE_HOLLOW | PIECE_SMALL | PIECE_SQUARE;
    
    QList<Piece *>::const_iterator it = fields.begin();
    for (; it != fields.end(); it++) {
        Piece *piece = *it;
        if (piece == NULL)
            return false;
        properties &= piece->getProperties();
        properties_invert &= ~piece->getProperties();
    }

    return properties || properties_invert;
}

/**
 * @brief Board::hasSameMatrix check if given board has same pieces in same matrix fieldss
 * @param board
 * @return
 */
bool Board::hasSameMatrix(Board *board)
{
    if (board == NULL)
        return false;

    for (unsigned i = 0; i < MATRIX_SIZE; ++i)
        for (unsigned j = 0; j < MATRIX_SIZE; ++j)
            if (matrix[i][j] != board->getMatrix()[i][j])
                return false;
    return true;
}

/**
 * @brief Board::reset resets everything so new game can start
 */
void Board::reset()
{
    stock.clear();
    // create all pieces
    for (unsigned char properties = 0; properties < PIECE_COUNT; properties++) {
        stock.push_back(new Piece(properties, this));
    }

    // allocate matrix
    matrix = new Piece**[MATRIX_SIZE];
    for (unsigned i = 0; i < MATRIX_SIZE; i++) {
        matrix[i] = new Piece*[MATRIX_SIZE];
        for (unsigned j = 0; j < MATRIX_SIZE; j++)
            matrix[i][j] = NULL;
    }
}
