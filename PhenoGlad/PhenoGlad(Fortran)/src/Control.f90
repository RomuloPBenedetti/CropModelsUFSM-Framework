
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

    PRINT*, 'FOR SELECTING CULTIVARS, TYPE 1'
    PRINT*, 'FOR SELECTING CYCLE DURATION, TYPE 2'
    READ*, cultivarType

    if (cultivarType .eq. 1) then
        PRINT*, 'INFORM A CULTIVAR (TYPE A CORRESPONDING NUMBER):'
        PRINT*, 'PURPLE FLORA = 1'
        PRINT*, 'ROSE FRIENDSHIP = 2'
        PRINT*, 'WHITE FRIENDSHIP = 3'
        PRINT*, 'T704 = 4'
        PRINT*, 'AMSTERDA = 5'
        PRINT*, 'PETER PEARS = 6'
        PRINT*, 'WHITE GODDESS= 7'
        PRINT*, 'GREEN STAR = 8'
        PRINT*, 'JESTER = 9'
        PRINT*, 'GOLD FIELD = 10'
        READ*, cultivar
    else
       PRINT*, 'INFORM THE CYCLE DURATION'
       PRINT*, 'EARLY = 1'
       PRINT*, 'INTERMEDIATE I = 2'
       PRINT*, 'INTERMEDIATE II = 3'
       PRINT*, 'LATE = 4'
       READ*, cycle
    endif


    PRINT*, 'INFORM IF YOU DESIRE TO START THE SIMULATION FROM CROP EMERGENCE &
             DATE (TRUE) OR FROM PLANTING DATE (FALSE):'

    read*, emergence

    PRINT*, 'INFORM THE YEAR YOU WANT TO START THE SIMULATION'
    read*, initYear

    print*, 'INFORM THE DAY OF THE YEAR YOU WANT TO START THE SIMULATION'
    read*, initDay

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

    if (cultivarType .eq. 1) then
        select case (cultivar)
            case(1)  ; call FARMING1  ()
            case(2)  ; call FARMING2  ()
            case(3)  ; call FARMING3  ()
            case(4)  ; call FARMING4  ()
            case(5)  ; call FARMING5  ()
            case(6)  ; call FARMING6  ()
            case(7)  ; call FARMING7  ()
            case(8)  ; call FARMING8  ()
            case(9)  ; Call FARMING9  ()
            case(10) ; call FARMING10 ()
        end select
    else
        select case (cycle)
            case(1) ; call CYCLE1 ()
            case(2) ; call CYCLE2 ()
            case(3) ; call CYCLE3 ()
            case(4) ; call CYCLE4 ()
        end select
    endif

    TBS = 5.0  ; TBV = 2.0  ; TBR = 6.0
    TOS = 25.0 ; TOV = 27.0 ; TOR = 25.0
    THS = 35.0 ; THV = 45.0 ; THR = 42.0
    lowTempCont = 0 ; lowLeavesTempCont = 0 ; lowDVSTempCont = 0
    hightTempCont = 0 ; hightTempContTotal = 0 ; i = 1
    V = nn ; H = nn ; F = nn ; N = nn ; DVSEnd = nn
    alfaS = log (2.) / log ((THS - TBS) / (TOS - TBS))
    alfaV = log (2.) / log ((THV - TBV) / (TOV - TBV))
    alfaR = log (2.) / log ((THR - TBR) / (TOR - TBR))
    simulating = .false. ; firstDay = .false.
    vegetative = .false. ; heading = .false. ; flowering = .false.
    leafAppearRate = .false.

    return
end

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
        if (.NOT. firstDay .AND. sprouting) then
            call DEVELOPMENTAL_STAGES (TBS, TOS, THS, alfaS, RS, rMaxS)
        endif

        if (.NOT. firstDay .AND. vegetative) then
            call DEVELOPMENTAL_STAGES (TBV, TOV, THV, alfaV, RV, rMaxV)
        endif

        if ((DVS(i) .GT. 0.0) .or. vegetative) then
            call CUMULATIVE_AND_FINAL_LEAF_NUMBER ()
        else
            call REPEAT_FINAL_LEAF_NUMBER ()
        endif

        if (heading) then
            call DEVELOPMENTAL_STAGES (TBR, TOR, THR, alfaR, RH, rMaxH)
        endif

        if (flowering) then
            call DEVELOPMENTAL_STAGES (TBR, TOR, THR, alfaR, RF, rMaxF)
        endif

        call WRITE_RESULTING_DATA ()
    endif

    call WARNINGS ()
    call CONTROL_STAGES ()

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

    firstDay = .false.
    if((year(i) .EQ. initYear) .AND. (day(i) .EQ. initDay)) then
        firstDay = .true.
        simulating = .true.
        if (emergence) then
            RV(i) = 0
            DVS(i) = 0
            rAcumV(i) = RV(i)
            sprouting = .false.
            vegetative = .true.
        else
            RS(i) = -1
            DVS(i) = -1
            rAcumS(i) = RS(i)
            sprouting = .true.
        endif
    endif

    return
end

!***********************************************************************
! Subrotina que controla os estágios de desenvolvimento,
! verifica quando um estágio termina e outro começa, é
! chamada logo após a verificação dos alertas. Também é responsável
! por verificar quando o ciclo de desenvolvimento acaba.
!
! Subrotine that controls the developmental stages, verifies
! when one stage ends and other starts. It is called right after warnings
! verification. It is also responsible for verifying when the cycle of
! development ends.
!***********************************************************************

subroutine CONTROL_STAGES ()
    use SEED

    if (i .GT. 1) then
         if ((DVS(i) .GT. 0.0) .AND. (DVS(i-1) .LE. 0.0)) then
            sprouting = .false.
            vegetative = .true.
            V = i
        endif

        if ((DVS(i) .GT. 0.8) .AND. (DVS(i-1) .LE. 0.8)) then
            vegetative = .false.
            heading = .true.
            H = i
        endif

        if ((DVS(i) .GT. 1.0) .AND. (DVS(i-1) .LE. 1.0)) then
            heading = .false.
            flowering = .true.
            F = i
        endif

        if ((DVS(i) .GT. 2.0) .AND. (DVS(i-1)  .LE. 2.0)) then
            simulating = .false.
            DVSEnd = i
        endif
    endif
    return
end

!***********************************************************************
! Subrotina que informa os diversos alertas por injuria ou morte
! devido a temperaturas extremas, verifica as condições todos os dias,
! apos simular e escrever o resultado desse dia.
!
! Subroutine that informs all the injury or death warnings due to extreme
! temperatures, it verifies the conditions every day after simulating and
! writing the results of each day.
!***********************************************************************

subroutine WARNINGS ()
    use SEED

    if(simulating) then
        if (.NOT.sprouting .AND. (tMin(i) .LT. -2.0)) then
            lowTempCont = lowTempCont + 1
            if (lowTempCont .GT. 3) then
                print*, "Freezing temperature reached: Crop killed by &
                         frost", day(i), DVS(i)
                simulating = .false.
            endif
        else
            lowTempCont = 0
        endif

        if (DVS(i) .GE. 0.64) then
            if ((tMin(i) .GT. -2.0) .AND. (tMin(i) .LT. 3.0)) then
                lowLeavesTempCont = lowLeavesTempCont + 1
                if (lowLeavesTempCont .GT. 3) then
                    print*, "Spike dead by frost" , day(i), DVS(i)
                    simulating = .false.
                endif
            else
                lowLeavesTempCont = 0
            endif

            if (tMin(i) .LE. -2.0) then
                print*, "Spike dead by frost", day(i), DVS(i)
                simulating = .false.
            endif
        endif

        if ((DVS(i) .GE. 0.8) .AND. (DVS(i) .LE. 1.05)) then
            if (tMax(i) .GE. 34.0) then
                hightTempCont = hightTempCont + 1
                if (hightTempCont .eq. 3) then
                    print*, "Risk of severe burning of  florets before harvest point &
                             and risk that the 3 or 4 uppermost florets on the  &
                             spike do not open", &
                             day(i), DVS(i)
                endif
            else
                hightTempCont = 0
            endif
        endif

        if ((DVS(i) .GE. 1.05) .AND. (DVS(i) .LE. 2.9)) then
            if (tMax(i) .GE. 34.0) then
                hightTempContTotal = hightTempContTotal + 1
                if (hightTempContTotal .eq. 3) then
                    print*, "Risk of severe burning of florets after harvest point &
                              and risk that the 3 or 4 uppermost florets on the  &
                             spike do not open", &
                             day(i), DVS(i)
                endif
            else
                hightTempContTotal = 0
            endif
        endif

        if (.NOT.sprouting .AND. (tMax(i) .GT. 48.0)) then
            print*, "Crop killed by heat.", day(i), DVS(i)
            simulating = .false.
        endif
    end if
    return
end


!***********************************************************************
! Subrotina que realiza a leitura dos dados meteorológicos do arquivo de
! entrada, lê uma linha toda vez que é chamada. O modelo chama a subrotina
! uma vez a cada dia, antes de iniciar qualquer outra tarefa nesse dia.
!
! Subrotine that reads the meteorological input file, it reads only one line
! each time it is called.
! The model calls the subroutine one time each day, before
! starting any other task at this day.
!***********************************************************************

subroutine READ_METEOROLOGIC_DATA_FROM_DAY ()
    use SEED

    read (1,*,IOSTAT = ioErr) year(i), day(i), tMin(i), tMax(i)
    tmed(i) = (tMin(i) + tMax(i)) / 2

    if (ioErr .LT. 0) then
        simulating = .false.
    end if

    return
end

!***********************************************************************
! Subrotina que escreve o cabeçalho no arquivo de saida. Escreve uma
! linha. O modelo chama apenas uma vez esta rotina antes de iniciar a
! simulação. Desta forma, o cabeçalho acaba sendo a primeira linha.
!
! Subrotine that writes the header on the result file. It writes only one line.
! The model calls subroutine only once before the simulation starts.
! Therefore, the header is the first line of the file.
!***********************************************************************

subroutine WRITE_HEADER_ON_RESULTS ()

     write (2,20) 'Dia', 'Ano', 'TMin','TMax','TMed', &
                  'fTMed', 'DVS', 'CLN', 'V_Stage'

  20 format (a3,2x,a4,2x,a4,2x,a4,2x,a4,2x,a8,2x,a8,2x, &
             a8,2x,a8)

     return
end

!***********************************************************************
! Subrotina que escreve os dados no arquivo de saida. Escreve uma linha
! toda vez que é chamada. O modelo chama a subrotina uma vez a cada dia.
! É chamada logo em seguida aos calculos.
!
! Subroutine that writes the result file. It writes only one line every time
! it is called. The model calls the subroutine once every day. It is
! called shortly after the calculations.
!***********************************************************************

subroutine WRITE_RESULTING_DATA ()
    use SEED

    write (2,12) day(i), year(i), tmin(i), tmax(i), tmed(i), &
                fTMed(i), DVS(i), CLN(i), vStage(i)

 12 format (i3, 2x, i4, 2x, f4.1, 2x, f4.1, 2x, &
            f4.1, 2x, f8.4, 2x, f8.4, 2x, f8.4, &
            2x, f4.1)

    return
end
