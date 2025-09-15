package internal

import (
	"log/slog"
	"os"
)

func NewStdOutLogger(verbose bool) *slog.Logger {
	level := slog.LevelInfo
	if verbose {
		level = slog.LevelDebug
	}
	return newLogger(os.Stdout, level)
}

func newLogger(file *os.File, level slog.Level) *slog.Logger {
	h := slog.NewJSONHandler(file, &slog.HandlerOptions{
		Level: level,
	})
	return slog.New(h)
}
