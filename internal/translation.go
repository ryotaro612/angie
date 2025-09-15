package internal

import (
	"context"
	"fmt"

	"github.com/modelcontextprotocol/go-sdk/mcp"
)

func MakeTranslationPrompt() mcp.Prompt {
	ja := mcp.PromptArgument{
		Name:        "ja",
		Title:       "A filepath to text in Japanese",
		Description: "The text to be translated",
		Required:    true,
	}

	prompt := mcp.Prompt{
		Arguments: []*mcp.PromptArgument{
			&ja,
		},
		Description: "Translate Japanese to English",
		Title:       "Translation Prompt",
	}

	return prompt
}

func MakeTranslationPromptHandler(ctx context.Context, req *mcp.GetPromptRequest) (*mcp.GetPromptResult, error) {
	params := req.Params
	ja := params.Arguments["ja"]

	msg := mcp.PromptMessage{
		Content: &mcp.TextContent{
			Text: fmt.Sprintf("Translate textx in %s to English.", ja),
		},
		Role: "user",
	}
	res := mcp.GetPromptResult{
		Description: "A prompt to translate Japanese to English",
		Messages: []*mcp.PromptMessage{
			&msg,
		},
	}
	return &res, nil
}
