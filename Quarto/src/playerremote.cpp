#include "playerremote.h"
#include <QPair>
#include <iostream>
#include <cassert>
#include <unistd.h>

PlayerRemote::PlayerRemote(QString name, QString _host, quint16 _port, Board *board, QObject *parent) :
    Player(name, board, parent), host(_host), port(_port), starts(false), lastLine()
{
    socket = new QTcpSocket(this);

    connect(socket, SIGNAL(connected()), this, SLOT(socketConnected()));

    // connect to server
    qDebug() << "Connecting to server " << host << "on port " << port;

    socket->connectToHost(host, port);
    if (!socket->waitForConnected(300)) {
        qDebug() << "Couldnt connect to server";
        exit(2);
    }

    welcome(QString("grans-lettol"));
}

/**
 * @brief PlayerRemote::getLineFromSocket
 * @return
 * Read content of socket, by using this function you can be sure that \n was read.
 */
QString PlayerRemote::getLineFromSocket()
{
    QString data;
    while (data.indexOf('\n') == -1) {
        if (!socket->bytesAvailable()) {
            socket->waitForReadyRead(-1);
        }
        char buffer[500] = {'\0'};
        socket->readLine(buffer, 499);
        data.append(buffer);
    }
    return data;
}

/**
 * @brief PlayerRemote::reset reset itself for next game.
 */
void PlayerRemote::reset()
{
    Player::reset();
    lastLine.clear();
}

/**
 * @brief PlayerRemote::choosePiece reads choosen piece from server
 * @return
 */
Piece *PlayerRemote::choosePiece()
{
    QString data;
    // SERVER: -1100 please send your move
    if (lastLine.size())
        data = lastLine.remove(0, lastLine.indexOf('-')+1);
    else
        data = getLineFromSocket();
    //std::cout << "SERVER: " << data.toStdString() << std::endl;


    QString pieceStr(data.left(4));
    Piece p(pieceStr);

    Piece *candidate = NULL;
    foreach (candidate, board->getStock())
        if(candidate->toBinaryString() == p.toBinaryString())
            break;

    return candidate;
}

/**
 * @brief PlayerRemote::sendPosition sends given possition to server
 * @param pos
 */
void PlayerRemote::sendPosition(QPair<unsigned, unsigned> pos)
{
    QString posStr(5);
    posStr[0] = pos.second + '0';
    posStr[1] = ',';
    posStr[2] = ' ';
    posStr[3] = pos.first  + '0';
    posStr[4] = '\n';

    // send position
    socket->write(posStr.toAscii());
    socket->flush();
    //std::cout << "CLIENT:" << posStr.toStdString() << std::endl;
}

/**
 * @brief PlayerRemote::sendPiece sends given piece to server
 * @param p
 */
void PlayerRemote::sendPiece(Piece *p)
{
    socket->write(p->toBinaryString().append('\n').toAscii());
    socket->flush();
    //std::cout << "CLIENT:" << p->toBinaryString().toStdString() << std::endl;

}

/**
 * @brief PlayerRemote::chooseField communicates with server and finds out what says
 * which piece should put on matrix and also gets answer where he did.
 * @param piece
 * @return
 */
QPair<unsigned, unsigned> PlayerRemote::chooseField(Piece *piece)
{
    QPair<unsigned, unsigned> pair;
    pair.second = -1;
    pair.first = -1;

    // only if it is not the first turn in game
    if (board->getFreeFields().size() != MATRIX_SIZE * MATRIX_SIZE) {
        sendPosition(board->lastMove);
    }

    // SERVER: please send piece in binary format
    QString data = getLineFromSocket();
    std::cout << "SERVER: " << data.toStdString() << std::endl;
    // send piece
    sendPiece(piece);

    // SERVER: 2, 2
    data.clear();
    data = getLineFromSocket();
    std::cout << "SERVER: " << data.toStdString() << std::endl;


    pair.second = data[0].toAscii() - '0';
    pair.first = data[3].toAscii() - '0';

    lastLine = data;

    return pair;
}

void PlayerRemote::closeConnection()
{
}


void PlayerRemote::socketReadyRead()
{
    socket->waitForReadyRead(-1);
    // read from the server

    QByteArray msg;
    msg = socket->readAll();

    // display message
    std::cout << "readyread " <<  msg.data() << std::endl;
}

void PlayerRemote::socketConnected()
{
    qDebug() << "Connected to server.\n";
}

void PlayerRemote::socketClosed()
{
}

void PlayerRemote::socketError()
{
    throw "ERROR: some error happend";
}

void PlayerRemote::welcome(QString  name)
{
    socket->waitForReadyRead(-1);

    QByteArray data = socket->readLine();
    std::cout << "SERVER: " << data.data();

    socket->write(name.append('\n').toAscii());
    socket->flush();

    socket->waitForReadyRead(-1);
    data = socket->read(100);
    socket->flush();
    std::cout << "SERVER: " << data.data() << std::endl;

    if (data.indexOf('1') == -1) // player2
        starts = true;
}
