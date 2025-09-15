package main

import (
	"context"
	"os"

	"github.com/modelcontextprotocol/go-sdk/mcp"
	"github.com/ryotaro612/angie/internal"
)

func main() {
	var err error
	cmd, err := internal.Parse(os.Args[1:])
	if err != nil {
		os.Exit(1)
		return
	} else if cmd.Help {
		cmd.PrintHelp()
		return
	}
	ctx := context.Background()
	logger := internal.NewStdOutLogger(cmd.Verbose)

	server := mcp.NewServer(&mcp.Implementation{
		Name:    "angie",
		Title:   "An mcp server for summarizing  papers",
		Version: internal.Version,
	}, &mcp.ServerOptions{
		HasPrompts: true,
		HasTools:   true,
	})

	defer func() {
		if err != nil {
			logger.ErrorContext(ctx, "error", "err", err)
			os.Exit(1)
		}
	}()

	logger.DebugContext(ctx, "starting server")

	// Run the server on the stdio transport.
	if err = server.Run(ctx, &mcp.StdioTransport{}); err != nil {
		return
	}

}
