#ifndef SERVER_H
#define SERVER_H

#include <QTcpServer>
#include <QTcpSocket>
#include <QObject>

class Server : public QObject
{
    Q_OBJECT

public:
    Server(quint64 _port, QObject* parent = 0);

private:
    QTcpServer *server;
    QTcpSocket *client1;
    QTcpSocket *client2;
    quint64 port;

signals:
    void readyWrite();

public slots:
    void acceptConnection();
    void readMsgFromClient();
    void sendMsgToClient();
    void sendInitMsg();
};

#endif // SERVER_H
