package main

import (
	"context"
	"fmt"
	"log/slog"
	"os"

	"github.com/modelcontextprotocol/go-sdk/mcp"
	"github.com/ryotaro612/angie/internal"
)

func main() {
	if args, err := internal.Parse(os.Args[1:]); err != nil {
		args.PrintHelp()
		fmt.Printf("doooo\n")
		if args.Help {
			os.Exit(1)
		}
		fmt.Printf("doooo2\n")
		return
	}

	ctx := context.Background()
	logger := internal.NewStdOutLogger(slog.LevelDebug)

	server := mcp.NewServer(&mcp.Implementation{
		Name:    "angie",
		Title:   "An mcp server for summarizing  papers",
		Version: internal.Version,
	}, &mcp.ServerOptions{
		HasPrompts: true,
		HasTools:   true,
	})

	var err error

	defer func() {
		if err != nil {
			logger.ErrorContext(ctx, "error", "err", err)
			os.Exit(1)
		}
	}()

	// Run the server on the stdio transport.
	if err = server.Run(ctx, &mcp.StdioTransport{}); err != nil {
		return
	}

}
