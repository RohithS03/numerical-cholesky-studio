JAVAC = javac
JAVA = java
SRC_DIR = src/main/java
TEST_DIR = src/test/java
OUT_DIR = bin

all: compile

compile:
	mkdir -p $(OUT_DIR)
	$(JAVAC) -d $(OUT_DIR) $(SRC_DIR)/com/numerical/studio/*/*.java $(SRC_DIR)/com/numerical/studio/*/*/*.java $(SRC_DIR)/com/numerical/studio/*.java $(TEST_DIR)/com/numerical/studio/*.java

test: compile
	$(JAVA) -cp $(OUT_DIR) com.numerical.studio.CholeskyTestRunner

run: compile
	$(JAVA) -cp $(OUT_DIR) com.numerical.studio.MainConsoleStudio

clean:
	rm -rf $(OUT_DIR)

.PHONY: all compile test run clean
