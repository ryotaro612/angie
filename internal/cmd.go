package internal

import (
	"flag"
	"fmt"
)

type Cmd struct {
	Help    bool
	Verbose bool
	fs      *flag.FlagSet
}

func Parse(args []string) (Cmd, error) {
	fs := flag.NewFlagSet("angie", flag.ContinueOnError)
	help := fs.Bool("help", false, "Show this message")
	verbose := fs.Bool("verbose", false, "Be verbose")
	if err := fs.Parse(args); err != nil {
		return Cmd{}, err
	}
	return Cmd{
		Help:    *help,
		Verbose: *verbose,
		fs:      fs,
	}, nil
}

func (c Cmd) PrintHelp() {
	fmt.Println("start\n")
	c.fs.PrintDefaults()
	fmt.Println("end\n")
}
