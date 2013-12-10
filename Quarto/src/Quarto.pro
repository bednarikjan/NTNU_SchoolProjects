#-------------------------------------------------
#
# Project created by QtCreator 2013-08-26T22:07:04
#
#-------------------------------------------------

QT       += core

QT       -= gui
QT       += network widgets

TARGET = Quarto
CONFIG   += console
CONFIG   -= app_bundle
#QMAKE_CXXFLAGS_DEBUG += -pg
#QMAKE_LFLAGS_DEBUG += -pg
TEMPLATE = app


SOURCES += main.cpp \
    game.cpp \
    board.cpp \
    player.cpp \
    piece.cpp \
    playerrandom.cpp \
    playernovice.cpp \
    playerminimax.cpp \
    playerremote.cpp \
    playerhuman.cpp

HEADERS += \
    game.h \
    board.h \
    player.h \
    piece.h \
    common.h \
    playerrandom.h \
    playernovice.h \
    playerminimax.h \
    optionparser/optionparser.h \
    playerremote.h \
    playerhuman.h
