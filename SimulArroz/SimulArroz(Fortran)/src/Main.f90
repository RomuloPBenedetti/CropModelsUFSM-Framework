
!***********************************************************************
! O modelo segue a seguinte sequencia de tarefas:
!    1) Inicia abrindo os arquivos de entrada e saída.
!    2) Escreve o cabeçalho no arquivo de saída.
!    3) Pede os dados de entrada para a simulação.
!    4) Preenche adequadamente algumas variáveis (define os coeficientes
!       da cultivar, valores de contadores, constantes, etc).
!    5) Realiza a simulação, fazendo verificações e cálculos dia apos
!       dia.
!
! The model does the following sequence of tasks:
!    1) Starts opening the input and output files.
!    2) Writes the header in the output file.
!    3) Asks the input data for simulation.
!    4) Fill some variables adequately (define the cultivar coefficients,
!       counter values, constants, etc).
!    5) Does the simulation, doing verifications and calculations
!       day after day.
!***********************************************************************

program UFSMModelsFramework
    use SEED
    CHARACTER (LEN=30)  :: outputFile

    open (1,file='files/meteorologicFile.txt')

    call READ_INPUT_FROM_KEYBOARD ()
    call FILL_PRE_SIMULATE_VARIABLES ()

    write (outputFile,'(A6, I4, A10)') "files/", initYear, "resultFile.txt"
    open (2,file = outputFile )

    call WRITE_HEADER_ON_RESULTS
    call SIMULATE ()

end program UFSMModelsFramework
