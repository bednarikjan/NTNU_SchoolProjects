#ifndef BOARD_H
#define BOARD_H

#include <QObject>
#include <QList>
#include <QPair>

#include "common.h"
#include "piece.h"

class Board : public QObject
{
    Q_OBJECT
public:
    explicit Board(QObject *parent = 0);
    Board(Board & other, QObject *parent = 0);

    ~Board();  
    /** Prints current board state with coordinates */
    void printMatrix();
    /** Prints current stock with options */
    void printStock();

    const QList<Piece *> & getStock() const { return stock; }
    Piece ***getMatrix() const { return matrix; }

    QList<QPair<unsigned, unsigned> > getFreeFields();
    bool checkVictory();

    void removeFromStockAt(unsigned index) { stock.removeAt(index); }
    void putPiece(const QPair<unsigned, unsigned> & index, Piece *piece);
    bool checkVictoryFields(const QList<Piece *> & fields);

    void deletePieceFromStock(Piece *piece) { stock.removeAll(piece); }
    void addPieceToStock(Piece *piece) { stock.append(piece); }
    void deletePiece(QPair<unsigned, unsigned> field) { matrix[field.first][field.second] = NULL; }

    bool hasSameMatrix(Board *board);
    QPair<unsigned, unsigned> lastMove;
    void reset();
private:
    QList<Piece *> stock;
    Piece ***matrix;

    /** Creates textual representation of the piece */
    QString piece2str(Piece *piece);

signals:
    
public slots:
    
};

#endif // BOARD_H
