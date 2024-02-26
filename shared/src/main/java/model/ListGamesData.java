package model;

import chess.ChessGame;

import java.util.Collection;

public record ListGamesData(Collection<ChessGame> games) {
}
