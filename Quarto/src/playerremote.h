#ifndef PLAYERREMOTE_H
#define PLAYERREMOTE_H
#include "player.h"
#include <QTcpSocket>
#include <QObject>

class PlayerRemote : public Player
{
    Q_OBJECT

public:
    PlayerRemote(QString name, QString _host, quint16 _port, Board *board, QObject *parent);
    Piece *choosePiece();

    bool hasFirstTurn() { return starts; }

    void sendPosition(QPair<unsigned, unsigned> pos);
    void sendPiece(Piece *p);

    QString getLineFromSocket();
    void reset();
protected:
    QPair<unsigned, unsigned> chooseField(Piece *piece);

private:
    QTcpSocket *socket;
    QString host;
    quint16 port;
    bool starts;
    QString lastLine;

    void welcome(QString name);

public slots:
    void closeConnection();
    void socketReadyRead();
    void socketConnected();
    //void socketConnectionClosed();
    void socketClosed();
    void socketError();
};

#endif // PLAYERREMOTE_H
