#include "player.h"

Player::Player(QString name, Board *board, QObject *parent) :
    QObject(parent), board(board), name(name)
{

}

/**
 * @brief Player::move plays given piece and choose another piece for opponent
 * @param piece to be placed
 * @return Choosen piece for opponent
 */
Piece *Player::move(Piece *piece)
{
    board->putPiece(chooseField(piece), piece);
    piece = choosePiece();
    board->deletePieceFromStock(piece);    
    return piece;
}
