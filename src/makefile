JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	CZeroController.java \
	CZeroLogicModule.java \
	CZeroMain.java \
	CZeroModel.java \
	CZeroView.java \
	MyFileManager.java 

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class
