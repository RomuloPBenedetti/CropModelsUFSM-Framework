
!***********************************************************************
! subrotina que pede para que o usuário entre com dados de entrada
! que definem as caracteristicas da simulação que será realizada dentro
! do periodo de tempo contido nos dados meteorológicos.
!
! Subroutine that asks the user to enter the data that define
! the simulation to be performed for the
! time period contained within the meteorological data.
!***********************************************************************

subroutine READ_INPUT_FROM_KEYBOARD ()
    use SEED

    return
end

!***********************************************************************
! Subrotina que é chamada antes de iniciar a simulação para preencher
! algumas variaveis com valores iniciais adequados para o modelo assim
! como algumas constantes.
!
! Subroutine called before the simulation starts to check some
! variables with initial values for the model as well as some constants
!***********************************************************************

subroutine FILL_PRE_SIMULATE_VARIABLES ()
    use SEED

    return
end

!***********************************************************************
! Subrotina que verifica quando o modelo deve iniciar a simulação,
! definindo se o modelo inicia no plantio
! ou na emergência.
!
! Subroutine that verifies when the model should start the simulation,
! defining if the model starts from planting or from
! emergence.
!***********************************************************************


!***********************************************************************
! Subrotina chamada para realizar a simulação, ignora a primeira linha
! do arquivo de entrada (cabeçalho) e em seguida chama a subrotina
! responsavel por realizar as tarefas de simulação que ocorrem a cada
! dia dos dados meteorológicos de entrada
!
! Subroutine called to realize the simulation, it ignores the first line of
! the input file (header) and then the subroutine
! responsible for doing the simulation tasks of simulation for each day
! of the input meteorological data.
!***********************************************************************

subroutine SIMULATE ()
    use SEED

    character (100) :: line
    read(1,'(A)') line

    do while ((i .LT. nn) .AND. (ioErr .EQ. 0))
        i = i + 1
        call DAY_OF_SIMULATION()
    end do

    if (ioErr .LT. 0) then
        PRINT*, 'SIMULATION FINISHED (ALL METEOROLOGICAL DATA WERE READ).'
    elseif (ioErr .GT. 0) then
        PRINT*, 'WARNING, SOMETHING IS WRONG WITH METEOROLOGICAL DATA'
    elseif (i == nn) then
        PRINT*, 'THE FILE WITH METEOROLOGICAL DATA IS TOO BIG, CAN NOT PROCESS.'
    endif

    return
end

!***********************************************************************
! Realiza as tarefas diárias do modelo, em ordem:
!   1) Le os dados meteorológicos do dia.
!   2) Verifica se a simulação chegou no primeiro dia.
!      3) Caso sim, entra nos estágios definidos pela subrotina de
!         controle de estágios e em seguida escreve os resultados
!         no arquivo de saida.
!   4) Verifica e, se necessário, dá os alertas e interrompe a
!      simulação, se necessário.
!   5) Verifica se o estágio de desenvolvimento atual deve parar e qual deveiniciar.
!
! Performs the daily tasks of the model, as follows:
!   1) Read the daily meteorological data.
!   2) Verify if simulation reached the first day.
!      3) If yes, go to the stages defined by the stage control subroutine
!         and after write the results in the output file.
!   4) Verify and, if necessary, give the warnings and stop the
!      simulation if necessary.
!   5) Verify if the actual developmental stage need to end and which one needs
!      to start.
!***********************************************************************

subroutine DAY_OF_SIMULATION ()
    use SEED

    call READ_METEOROLOGIC_DATA_FROM_DAY ()
    call PRE_SIMULATION_CHECKS ()

    if (simulating) then
        call PLANT_PROCESSES ()
        call WRITE_RESULTING_DATA ()
    endif

    call WARNINGS ()
    call CONTROL_STAGES ()

    return
end

!***********************************************************************
!
!
!
!***********************************************************************

subroutine PLANT_PROCESSES ()
    use seed

    return
end

!***********************************************************************
! Subrotina que verifica quando o modelo deve iniciar a simulação,
! definindo se o modelo inicia no plantio
! ou na emergência.
!
! Subroutine that verifies when the model should start the simulation,
! defining if the model starts from planting or from
! emergence.
!***********************************************************************

subroutine PRE_SIMULATION_CHECKS ()
    use SEED

    return
end
!***********************************************************************
! Subrotina que controla os estágios de desenvolvimento,
! verifica quando um estágio termina e outro começa, é
! chamada logo após a verificação dos alertas. Também é responsável
! por verificar quando o ciclo de desenvolvimento acaba.
!
! Subrotine that controls the developmental stages, verifies
! when one stage ends and other starts. It is called right after
! warnings verification. It is also responsible for verifying when the
! cycle of development ends.
!***********************************************************************

subroutine CONTROL_STAGES ()
    use SEED

    return
end

!***********************************************************************
! Subrotina que informa os diversos alertas por injuria ou morte
! devido a temperaturas extremas, verifica as condições todos os dias,
! apos simular e escrever o resultado desse dia.
!
! Subroutine that informs all the injury or death warnings due to
! extreme temperatures, it verifies the conditions every day after
! simulating and writing the results of each day.
!***********************************************************************

subroutine WARNINGS ()
    use SEED

    return
end


!***********************************************************************
! Subrotina que realiza a leitura dos dados meteorológicos do arquivo de
! entrada, lê uma linha toda vez que é chamada. O modelo chama a
! subrotina uma vez a cada dia, antes de iniciar qualquer outra tarefa
! nesse dia.
!
! Subrotine that reads the meteorological input file, it reads only one
! line each time it is called. The model calls the subroutine one time
! each day, before starting any other task at this day.
!***********************************************************************

subroutine READ_METEOROLOGIC_DATA_FROM_DAY ()
    use SEED

    return
end

!***********************************************************************
! Subrotina que escreve o cabeçalho no arquivo de saida. Escreve uma
! linha. O modelo chama apenas uma vez esta rotina antes de iniciar a
! simulação. Desta forma, o cabeçalho acaba sendo a primeira linha.
!
! Subrotine that writes the header on the result file. It writes only
! one line. The model calls subroutine only once before the simulation
! starts. Therefore, the header is the first line of the file.
!***********************************************************************

subroutine WRITE_HEADER_ON_RESULTS ()


     return
end

!***********************************************************************
! Subrotina que escreve os dados no arquivo de saida. Escreve uma linha
! toda vez que é chamada. O modelo chama a subrotina uma vez a cada dia.
! É chamada logo em seguida aos calculos.
!
! Subroutine that writes the result file. It writes only one line every
! time it is called. The model calls the subroutine once every day. It
! is called shortly after the calculations.
!***********************************************************************

subroutine WRITE_RESULTING_DATA ()
    use SEED

    return
end
