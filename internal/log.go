package internal

import (
	"log/slog"
	"os"

	"log"
)

func NewStdOutLogger(level slog.Level) *log.Logger {
	return newLogger(os.Stdout, level)
}

func newLogger(file *os.File, level slog.Level) *log.Logger {
	h := slog.NewJSONHandler(file, nil)
	return slog.NewLogLogger(h, level)
}
