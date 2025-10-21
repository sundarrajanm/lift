# Makefile for Smart Commit CLI

# Default Gradle command
GRADLE = ./gradlew

# Gradle options
GRADLE_OPTS = --quiet

# Build the fat JAR
build:
	$(GRADLE) shadowJar $(GRADLE_OPTS)

# Install the CLI locally (~/.local/bin)
install: build
	$(GRADLE) installTool $(GRADLE_OPTS) --no-configuration-cache

# Clean build artifacts
clean:
	$(GRADLE) clean $(GRADLE_OPTS)

# Run smart-commit tool (default llama3)
run:
	$(GRADLE) run $(GRADLE_OPTS) --args="llama3"

# Run smart-commit tool with custom model
run-model:
	$(GRADLE) run $(GRADLE_OPTS) --args="$(MODEL)"

# Show available tasks
help:
	@echo "Makefile commands:"
	@echo "  make build        - Build the smart-commit fat JAR"
	@echo "  make install      - Install the CLI locally (~/.local/bin)"
	@echo "  make clean        - Clean build artifacts"
	@echo "  make run          - Run smart-commit CLI (default model: llama3)"
	@echo "  make run-model MODEL=<model> - Run smart-commit CLI with custom model"
	@echo "  make help         - Show this help message"
