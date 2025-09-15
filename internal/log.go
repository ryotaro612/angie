package internal

import (
	"log/slog"
	"os"
)

func NewStdOutLogger(level slog.Level) *slog.Logger {
	return newLogger(os.Stdout, level)
}

func newLogger(file *os.File, level slog.Level) *slog.Logger {
	h := slog.NewJSONHandler(file, &slog.HandlerOptions{
		Level: level,
	})
	return slog.New(h)
}
