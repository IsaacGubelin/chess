package model;

import chess.ChessGame;

public record GameRequestData(String gameName, ChessGame.TeamColor playerColor, int gameID) {
}
