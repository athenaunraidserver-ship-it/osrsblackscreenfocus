# Black Screen Focus (RuneLite plugin)

Shrinks the game to a % of the RuneLite window and leaves the rest black. The window and sidebar stay full size.

## How it works
RuneLite wraps the game canvas in a black `ClientPanel`. The plugin puts an empty padding border on that panel, so the canvas really gets smaller and RuneLite's own mouse mapping stays accurate. Disabling the plugin restores the original border.

## Settings
Width %, Height % (or follow width), horizontal/vertical position, empty-space colour, toggle hotkey (small <-> full).

## Use with Stretched Mode
- **Resizable mode:** you just get a smaller viewport at 100% scale.
- **Stretched Mode plugin on:** the game scales down to fit the smaller canvas. This is the "same game, smaller" look you probably want. Fixed-size classic layout without stretching will clip instead.

## Build and test
```
set JAVA_HOME=<path to JDK 17>      (a portable one is in .tools\)
gradlew test build
gradlew run        # dev client with the plugin preloaded
```

## Run it in your normal RuneLite (Jagex account)
`gradlew run` launches a dev client that needs credentials. Run the official launcher once with `--insecure-write-credentials`, log in, then `gradlew run` reads `~/.runelite/credentials.properties`. Delete that file when you're done: it stores a login token in plain text.

## Publish to the Plugin Hub
1. Put this folder in its own public GitHub repo.
2. Fork `runelite/plugin-hub`, add `plugins/black-screen-focus` containing `repository=<your repo url>` and `commit=<full commit sha>`.
3. Open a PR; RuneLite reviewers check it. Re-PR a new commit sha for each update.
