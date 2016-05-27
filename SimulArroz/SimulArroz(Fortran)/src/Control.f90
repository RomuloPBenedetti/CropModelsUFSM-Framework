
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

     PRINT*, 'INFORME A DATA DE SEMEADURA (DIA DO ANO).'
     READ*, initDay
     
     PRINT*, 'INFORME O ANO DE INÍCIO DA SIMULAÇÃO'
     READ*, initYear
     
     PRINT*, 'INORME O ULTIMO ANO DE SIMULAÇÃO'
     READ*, lastYear
     
     PRINT*, 'INFORME A CONCENTRAÇÃO DE CO2 NA ATMOSFERA (ppm):'
     READ*, CO2
     
     PRINT*, 'PARA ESCOLHER CULTIVARES, DIGITE 1'
     PRINT*, 'PARA ESCOLHER GRUPOS DE MATURA��O, DIGITE 2'
     READ*, resposta1
     
     if (resposta1.EQ.1)  then
         PRINT*, 'INFORME A CULTIVAR (DIGITE O NUMERO CORRESPONDENTE):'
         PRINT*, 'IRGA421 = 1'
         PRINT*, 'IRGA416 = 2'
         PRINT*, 'BRS QUERENCIA = 3'
         PRINT*, 'IRGA417 = 4'
         PRINT*, 'IRGA420 = 5'
         PRINT*, 'BRIRGA409 = 6'
         PRINT*, 'BRS7TAIM = 7'
         PRINT*, 'IRGA424 = 8'
         PRINT*, 'TIO TAKA = 9'
         PRINT*, 'EPAGRI109 = 10'
         PRINT*, 'EEA406 = 11'
         PRINT*, 'INOV CL = 12'
         PRINT*, 'QM1010 CL = 13'
         PRINT*, 'PRIME CL = 14'
         READ*, cultivar
     endif
     
     if  (resposta1.EQ.2) then
         PRINT*, 'INFORME O GRUPO DE MATURAÇÃO):'
         PRINT*, 'MUITO PRECOCE=1'
         PRINT*, 'PRECOCE=2'
         PRINT*, 'MÉDIO=3'
         PRINT*, 'TARDIO=4'
         READ*, grupoMaturacao
     endif
     
     PRINT*,'INFORME A DENSIDADE DE PLANTAS (número de plantas por m2)'
     READ*, populacao
     
     PRINT*, 'INFORME O NÍVEL TECNOLÓGICO DA LAVOURA:'
     PRINT*, 'POTENCIAL = 1'
     PRINT*, 'ALTO = 2'
     PRINT*, 'MÉDIO = 3'
     PRINT*, 'BAIXO = 4'
     READ*,  resposta3
 
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
    
    if (resposta1 .EQ. 1) then
        select case (cultivar)
            case(1)  ; call CULTIVAR1  ()
            case(2)  ; call CULTIVAR2  ()
            case(3)  ; call CULTIVAR3  ()
            case(4)  ; call CULTIVAR4  ()
            case(5)  ; call CULTIVAR5  ()
            case(6)  ; call CULTIVAR6  ()
            case(7)  ; call CULTIVAR7  ()
            case(8)  ; call CULTIVAR8  ()
            case(9)  ; call CULTIVAR9  ()
            case(10) ; call CULTIVAR10 ()
            case(11) ; call CULTIVAR11 ()    
            case(12) ; call CULTIVAR12 ()    
            case(13) ; call CULTIVAR13 ()    
            case(14) ; call CULTIVAR14 ()    
        end select
    else
        select case (grupoMaturacao)
            case(1) ; call MUITOPRECOCE ()
            case(2) ; call PRECOCE ()
            case(3) ; call MEDIO ()
            case(4) ; call TARDIO ()
        end select
    endif

    TBDV = 11.0  ; TODV = 30.0  ; TMDV = 40.0
    TBDR = 15.0 ; TODR = 25.0 ; TMDR = 35.0
    TBDG = 15.0 ; TODG = 23.0 ; TMDG = 35.0
    TCMIN = 11.0 ; TCOT = 26.0 ; TCMAX = 40.0
    E=0.65924569
    lowTempCont = 0 ; hightTempCont = 0 ; i = 0
    V = nn ; R = nn ; G = nn ; N = nn ; DVSEnd = nn
    alfaS = 2*((TMED-TCMIN)**ALFA)
    alfaV = log (2.) / log ((THV - TBV) / (TOV - TBV))
    alfaR = log (2.) / log ((THR - TBR) / (TOR - TBR))
    simulating = .false. ; firstDay = .false.
    vegetative = .false. ;  reprodutive = .false. grainfill = .false.
    
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
    
    if  ((initYear.EQ.year(i))  .AND. (initDay.EQ.day(i)) ) then
        firstDay = .true.
        simulating = .true.
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
    
    read (1,*,IOSTAT = ioErr) year(i), day(i), tmin(i), tmax(i), radSol(i)   
    tmed(i) = (tmin(i) + tmax(i)) / 2 
    
    if (ioErr . LT. 0) then
        simulating = .false.
    end if 
    
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
    
    write (2,20) 'IANO IDIA TMIN TMAX TMED RADSOL STVG STRP STEG &
                   DVS DREM DRVG DRRP DREG HS GCROP RWLVG WLVG &
                   RWSTG WSTG RWSOG WSOG RWRTG WRTG LAI DLV &
                   DST SODAY TOTALSO PESODIA PESO1 PESOG DAS'

 20 format (i4, 1x,i3, 1x,f5.1, 1x,f5.1, 1x,f5.1, 1x,f4.1, 1x,f6.2, 1x,f6.2, &
            1x,f6.2, 1x,f7.4, 1x,f7.4, 1x,f7.4, 1x,f7.4, 1x,f7.4, 1x,f4.1, 1x,f10.4, &
            1x,f10.4, 1x,f10.4, 1x,f10.4, 1x,f10.4, 1x,f10.4, 1x,f10.4, 1x,f10.4,1x, &
            F10.4, 1x,f6.2, 1x,f5.2, 1x,f5.2, 1x,f12.2, 1x,f12.2, 1x,f7.4, 1x,f7.4,1x, &
            F6.1, 1x,i3)
     
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
        
    write (2,30) IANO, IDIA, TMIN, TMAX, TMED, RADSOL, STVG, STRP, & 
                STEG,  DVS, DREM, DRVG, DRRP, DREG, HS, GCROP, RWLVG, &
                WLVG, RWSTG, WSTG, RWSOG, WSOG, RWRTG, WRTG, LAI, &
                 DLV, DST, SODAY, TOTALSO, PESODIA, PESO1, PESOG, DAS
                 
  30 format  (i4, 1x,i3, 1x,f5.1, 1x,f5.1, 1x,f5.1, 1x,f4.1, 1x,f6.2, 1x,f6.2, &
             1x,f6.2, 1x,f7.4, 1x,f7.4, 1x,f7.4, 1x,f7.4, 1x,f7.4, 1x,f4.1, 1x,f10.4, &
             1x,f10.4, 1x,f10.4, 1x,f10.4, 1x,f10.4, 1x,f10.4, 1x,f10.4, 1x,f10.4,1x, &
             F10.4, 1x,f6.2, 1x,f5.2, 1x,f5.2, 1x,f12.2, 1x,f12.2, 1x,f7.4, 1x,f7.4,1x, &
             F6.1, 1x,i3)
             
    return
end
