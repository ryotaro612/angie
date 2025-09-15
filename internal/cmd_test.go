package internal

import (
	"testing"
)

func TestMainAcceptsHelp(t *testing.T) {
	t.Parallel()

	if res, err := Parse([]string{"-help"}); err != nil {
		t.Errorf("parsing error: %v", err)
	} else if !res.Help {
		t.Errorf("Help was not accepted: %v", err)
	}
}
