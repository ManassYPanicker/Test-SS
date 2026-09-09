#!/bin/bash
# Nuke CommonComponents.kt and rebuild it correctly from scratch based on git

# First lets try to restore it from our initial state if possible. 
# We'll just fetch a clean version from git.
git checkout app/src/main/java/com/example/ui/components/CommonComponents.kt || true

