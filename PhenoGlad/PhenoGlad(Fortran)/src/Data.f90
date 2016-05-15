
module SEED
    integer*4, parameter :: nn = 36500

    logical*2 emergence
    integer*2 cultivarType, cultivar, cycle, initYear, initDay

    integer*2 year(nn), day(nn) ; real*4 tmin(nn), tmax(nn)
    integer*4 i, ioErr

    logical*2 simulating, firstDay

!***********************************************************************
! Nome das fases de desenvolvimento de gladíolo calculadas pelo modelo:
! brotação (sprouting), vegetativa (vegetative), espigamento (heading) e
! florescimento (flowering). Cada uma destas fases possui temperaturas
! cardinais específicas e coeficientes genético-específicos
!
! Name of the developmental phases in gladiolus calculated with the model:
! sprouting phase, vegetative phase, heading sub-phase, and flowering sub-phase.
! Each phase and sub-phase has its own cardinal temperatures and genetic-
! specific coefficients.
!***********************************************************************

    logical*2 sprouting, vegetative, heading, flowering, leafAppearRate
    integer*4 V, H, F, N, DVSEnd

!***********************************************************************
! Estes são os contadores para a parte dos danos por altas e baixas
! temperaturas
!
! These are the counters used in the warnings due to low and high temperatures.
!***********************************************************************

    integer*2 hightTempCont, lowLeavesTempCont, lowDVSTempCont 
    integer*2 hightTempContTotal, lowTempCont

!***********************************************************************
! Temperaturas cardinais usadas para o cálculo em cada fase de desenvolvimento:
! brotação,
! (TBS, TOS, THS), vegetativa e emissão de folhas (TBV, TOV,
! THV) e reprodutiva (TBR, TOR e THR)
!
! Cardinal temperatures used in each developmental phase: sprouting
! (TBS, TOS, THS), vegetative and leaf appearance (TBV, TOV, THV), and
! reproductive (TBR, TOR e THR).
!***********************************************************************

    real*4 TBS, TOS, THS, TBV, TOV, THV, TBR, TOR, THR

!***********************************************************************
! Cálculo do coeficiente alfa da função beta para as fase de brotação,
! vegetativa e reprodutiva; eles são usados no cálculo da função de resposta à
! temperatura
!
! Calculating the alpha coefficient of the beta function for the sprouting,
! vegetative and reproductive phases used in the calculation of the temperature
! response function.
!***********************************************************************

    real*4 alfaS, alfaV, alfaR

!***********************************************************************
! Taxa máxima de desenvolvimento da fase de brotação (rmaxv), vegetativa
! (rmaxv), de espigamento (rmaxh) e de florescimento (rmaxf) LARmax:
! máxima taxa de aparecimento de folhas. CF:
! fator de correção somado ao número de folhas quando o DVS=0.8.
!
! Maximum developmental rate of the sprouting phase (rmaxv), vegetative
! phase (rmaxv), heading phase (rmaxh), and flowering phase (rmaxf).
! LARmax: maximum leaf appearance rate. CF: correction factor added to the
! cumulative leaf number when DVS=0.8.
!***********************************************************************

    real*4 rMaxV, rMaxF, rMaxS, rMaxH, LARMax, CF

!***********************************************************************
! Temperatura média diária do ar (tmed), função de resposta à
! temperatura (fTMed) e função de resposta à temperatura para as
! fases de desenvolvimento da cultura do gladíolo listadas acima.
!
! Mean daily air temperature (tmed), temperature response function (fTMed)
! and temperature response functions for the developmental phases above
! cited.
!***********************************************************************

    real*4 tmed(nn), fTMed(nn), fTMedS(nn), fTMedV(nn), fTMedL(nn)
    real*4 fTMedR(nn)

!***********************************************************************
! Taxa diária de desenvolvimento para a fase de brotação (RS), fase
! vegetativa (RV), fase de espigamento (RH) e fase de florescimento (RF).
!
! Daily developmental rate during the sprouting phase (RS), vegetative phase
! (RV), heading sub-phase (RH), and flowering (RF).
!***********************************************************************

    real*4 RV(nn), RF(nn), RH(nn), RS(nn)

!***********************************************************************
! Taxa de desenvolvimento acumulado para as diferentes fases de
! desenvolvimento listadas acima.
!
! Accumulating developmental rates during the developmental phases above cited.
!***********************************************************************

    real*4 rAcumS(nn), rAcumV(nn), rAcumH(nn), rAcumF(nn), rAcumE(nn)
    real*4 rAcumRH(nn), rAcumRF(nn), rAcum(nn)

!***********************************************************************
! Estágios de desenvolvimento (DVS), comandam o funcionamento do modelo.
! CLN: número de folhas acumuladas diariamente. FLN: número final de
! folhas. LAR: taxa diária de aparecimento de folhas. vStage: estágios
! vegetativos, é o resultado do arredondamento dos valores de CLN.
!
! Developmental stage (DVS), controls the model clock. CLN: cumulative
! leaf number. FLN: final leaf number. LAR: daily leaf appearance rate.
! vStage: vegetative stage, which is a result of rounding CLN.
!***********************************************************************

    real*4 DVS(nn), CLN(nn), FLN(nn), LAR(nn), vStage(nn)
end module
