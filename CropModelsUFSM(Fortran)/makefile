
# debugging makefile, use it only for developmente compilation
# as it uses the worst optimizations mode, but at all avoid
# the compiller to make undebugable optimizations
#
# base directories names
#
# build - where intermediate objects files are generated
# dist - where the executable distributable is generated
# src - where the source code is found
# debug - means this makefile is for debugging compilation
# linux - means this makefile is intended for linux plataform

SRCDIR = src
BUILDDIR = build
DISTDIR = dist

CONF = debug
PLATFORM = linux

# base system components
# programs used by make, compiler, linker, etc

MKDIR = mkdir
RM = rm
CP = cp
FCOMPILER = gfortran

# compiler flags
FCFLAGS = -Og -g -fno-automatic -static -fbounds-check
FCFLAGS += -fbacktrace -pedantic -Wextra  -Wall
FCFLAGS += -I/usr/include

# directorie names and macros
# macros are reductions of names or predefined values concatenated

OBJECTDIR = ${BUILDDIR}/${CONF}/${PLATFORM}
BINDIR = ${DISTDIR}/${CONF}/${PLATFORM}

OBJECTFILES = ${OBJECTDIR}/Data.o \
			  ${OBJECTDIR}/Main.o \
			  ${OBJECTDIR}/Model.o \
			  ${OBJECTDIR}/Control.o \
			  ${OBJECTDIR}/Farmings.o

PROGRAM = ${BINDIR}/PhenoGlad

# compilation rules
# recipes for compiling of each object and linking of the program
#  target: requisites
#  	operation

${PROGRAM}: ${OBJECTFILES}
	${MKDIR} -p ${BINDIR}
	${FCOMPILER} ${FCFLAGS} -o $@ $^ -static

# % is a wildcard meaning anything
# $@ the name of the _target_ file of the rule
# $< The names of first the prerequisites
# $^ The names of all the prerequisites

${OBJECTDIR}/%.o: ${SRCDIR}/%.f90
	${MKDIR} -p ${OBJECTDIR}
	${FCOMPILER} ${FCFLAGS} -c -o $@ $<

clean :
	${RM} -fr ${BUILDDIR}
	${RM} -fr ${DISTDIR}
	${RM} -f seed.mod
