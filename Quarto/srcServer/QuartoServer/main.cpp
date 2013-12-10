#include <QtCore/QCoreApplication>
#include <QStringList>
#include "server.h"

int main(int argc, char *argv[])
{
    QCoreApplication a(argc, argv);
    bool ok;
    if((argc != 3) || (a.arguments().at(1)!="-p"))
    {
        qDebug() << "Bad run, usage: ./server2012 -p PORT";
        return 1;
    }

    quint16 port = a.arguments().at(2).toInt(&ok);
    if (!ok) {
        qDebug() << "Bad run, usage: ./server2012 -p PORT";
        return 1;
    }

    try {
        Server server(port);
        return a.exec();
    } catch (const char* error) {
        qDebug() << error;
    }
}
