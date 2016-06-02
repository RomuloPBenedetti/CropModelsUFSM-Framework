!***********************************************************************
!Subrotina que calcula os estágios de desenvolvimento: fase de brotação (do
!DVS=-1 até DVS=0), fase vegetativa (do DVS=0 até DVS=0.8), e fase reprodutiva,
!que envolve as sub-fases de espigamento (DVS=0.8 até DVS=1) e florescimento
!(DVS=1 até 2). Cada uma destas fase utiliza temperaturas cardinais específicas.
! Para cada dia, esta subrotina calcula uma
!taxa de desenvolvimento diária (r), calculada a partir da multiplicação
!entre o coeficiente genético-específico rmax e a função de resposta à
!temperatura média (fTMed). A taxa de desenvolvimento
!acumulada é calculada a partir do acúmulo da taxa de desenvolvimento diária
!para cada fase de desenvolvimento, sempre acumulando com as fases anteriores.
!*******************************************************************

subroutine TEMPERATURE_FUNCTION (TCMIN, TCOT, TCMAX)
    use SEED
    real TCMIN, TCOT, TCMAX

    if (tmin(i) .GT. ) then
        STTMINV = (TODV-TBDV)*((TMDV-TMIN)/(TMDV-TODV))
    else
        STTMINV = TMIN - TBDV
        
            
    endif
            
    return
end

!***********************************************************************
!
!***********************************************************************

subroutine CRONOLOGICAL_FUNCTION 
    use SEED
    real TCMIN, TCOT, TCMAX
    
    if (HS(i) .LE. 2.0) then
        FC1 = 1.0
    elseif (HS .GE. 2.0) then
        FC1 = (HS / 2) ** (-0.3)
    endif
    
    return
end

!***********************************************************************
! Cálculo do número de folhas acumuladas (CLN) que ocorre durante a fase
! vegetativa, do DVS=0 até o DVS=0.8. O número de folhas acumulado
! inicia no dia da emergência e acaba quando o DVS=0.8, ou seja, no dia
! do R1. Neste dia, o modelo pára de emitir folhas e soma o último valor
! calculado ao fator de correção (CF), espcífico para cada cultivar.
! Neste momento, é definido o número final de folhas (FLN). A coluna do
! vStage é o arredondamento dos valores de CLN acumulados diariamente.
!
! Calculating the cumulative leaf number (CLN) during the vegetative
! phase, from DVS=0 to DVS=0.8. The accumulation starts at the day
! of emergence and ends when DVS=0.8, i.e. at the day of R1. On this
! day the model stops the appearance of leaves and adds the genotype-
! specific correction factor (CF) to the CLN, which defines the final
! leaf number (FLN). The vStage is the result of rounding CLN.
!***********************************************************************

subroutine CUMULATIVE_AND_FINAL_LEAF_NUMBER ()
    use SEED
    real TCMIN, TCOT, TCMAX

    if (tmed(i) .LE. TCMIN) then
        fTMed(i) =0
    elseif ((tmed(i) .GE. TCMIN) .AND. (tmed(i) .LE. TCMAX)) then
        fTMed(i) = (2 *  ((tmed(i) - TCMIN) ** (alfa)) * &
                            ((TCOT - TCMIN) ** (alfa)) - &
                            ((tmed(i) - TCMIN) ** (2 * alfa)) &
                    ) /       ((TCOT - TCMIN) ** (2 * alfa))
    elseif (tmed(i) .GT. TCMAX) then
        fTMed(i) = 0.0
    endif
    
    

    LAR(i) = LARMax * fTMedL(i)
    CLN(i) = CLN(i-1) + LAR(i)

    if ((DVS(i) .GT. 0.8) .AND. (DVS(i-1) .LT. 0.8)) then
      N = i
      CLN(i) = CLN(N) + CF
      FLN(i) = CLN(i)
      FLN(i) = CLN(N)
    endif

    if (DVS(i) .LE. 0.8) then
        vStage(i) = CLN(i)
        vStage(i) = NINT (CLN(i))
    else
        vStage(i)= NINT(CLN(N))
    endif

    return
end
