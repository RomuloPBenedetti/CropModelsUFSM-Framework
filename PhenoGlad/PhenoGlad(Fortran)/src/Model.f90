
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
!
! Subroutine that calculates the developmental stages: sprounting phase
! (from DVS=-1 to DVS=0), vegetative phase (from DVS=0 to DVS=0.8),
! and reproductive phase (DVS=0.8 to DVS=2.0). Each of these phases
! different cardinal temperatures. Each day, the subroutine calculates
! the daily developmental rate (R), which is calculated by multiplying
! the genetic-specific coefficients rmax by the temperature response
! function (fTMed). The accumulated developmental rate is calculated
! by accumulating the daily developmental rate in each developmental
! phase.
!***********************************************************************

subroutine DEVELOPMENTAL_STAGES (TB, TO, TH, alfa, R, rMax)
    use SEED
    real TB, TO, TH, alfa, R(nn), rMax

    if (tmed(i) .LT. TB) then
        fTMed(i) = 0.0
    elseif ((tmed(i) .GE. TB) .AND. (tmed(i) .LE. TH)) then
        fTMed(i) = (2 *  ((tmed(i) - TB) ** (alfa)) * &
                         ((TO - TB) ** (alfa)) - &
                         ((tmed(i) - TB) ** (2 * alfa)) &
                    ) /  ((TO - TB) ** (2 * alfa))
    elseif (tmed(i) .GT. TH) then
        fTMed(i) = 0.0
    endif

    R(i) = rMax * fTMed(i)
    DVS(i) = DVS(i-1) + R(i)

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

    if (tmed(i) .LT. TBV) then
        fTMedL(i) = 0.0
    elseif ((tmed(i) .GE. TBV) .AND. (tmed(i) .LE. THV)) then
        fTMedL(i) = (2 * ((tmed(i) - TBV) ** (alfaV)) * &
                         ((TOV - TBV) ** (alfaV)) - &
                         ((tmed(i) - TBV) ** (2 * alfaV)) &
                    ) /  ((TOV - TBV) ** (2 * alfaV))
    elseif (tmed(i) .GT. THV) then
        fTMedL(i) = 0.0
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

!***********************************************************************
! Subrotina responsável por repetir o número final de folhas após o seu
! cálculo, que ocorre quando o DVS=0.8. Da mesma forma, a coluna dos
! vStages fazem a mesma coisa. Isso foi implementado em alusão ao campo,
! pois quando a planta emite a última folha, permanece com o mesmo
! número de folhas até o final do ciclo.
!
! Subroutine responsable for repeating the final leaf number (FLN)
! after it is defined, which occurs at DVS=0.8. Likewise, the vStage
! is also repeated. This was implemented to mimic what happens in
! the field as the plant stops emitting leaves when FLN is defined.
!***********************************************************************

subroutine REPEAT_FINAL_LEAF_NUMBER ()
    use SEED

    CLN(i) = CLN(i-1)
    vStage(i) = vStage(i-1)

    return
end
