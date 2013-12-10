#include "server.h"
#include <iostream>

Server::Server(quint64 _port, QObject *parent) : QObject(parent), port(_port)
{
    server = new QTcpServer(this);
    connect(server, SIGNAL(newConnection()), this, SLOT(acceptConnection()));   
    connect(this, SIGNAL(readyWrite), this, SLOT(sendInitMsg));
    server->setMaxPendingConnections(2);

    qDebug() << "port = " << port;

    if(!server->listen(QHostAddress::Any, port)) {
        throw "ERROR: server can't listen";
    }
}


void Server::acceptConnection()
{
    client1 = server->nextPendingConnection();
    if(!client1)
        throw "ERROR: Could not create socket for connecting client.";

    qDebug() << "Client connected to server\n";

    connect(client1, SIGNAL(readyRead()), SLOT(readMsgFromClient()));
//    connect(socket, SIGNAL(disconnected()), SLOT(sdisconnected()));

    sendInitMsg();
}

void Server::readMsgFromClient()
{
    QByteArray msg;

    // read message
    msg = client1->readAll();

    // display message
    char *data = msg.data();
    while(*data) {
        std::cout << *data << std::endl;
        ++data;
    }

    // process it

    // reply
    sendMsgToClient();
}

void Server::sendMsgToClient()
{

}

/** Initial message for client */
void Server::sendInitMsg()
{
    qDebug() << "posilam hello";
    client1->write("hello client1");
}
