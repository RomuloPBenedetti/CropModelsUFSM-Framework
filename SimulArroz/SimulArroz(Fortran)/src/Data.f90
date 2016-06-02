
module SEED
    
    integer*4, parameter :: nn = 36500
        
    real*4 tmed(nn), tmin(nn), tmax(nn), radSol(nn) ; integer*2 year(nn), day(nn)
    integer*2 initYear, lastYear, initDay ; integer*4 i, ioErr 
    
    integer*4 CO2, resposta1, grupoMaturacao, cultivar,  populacao, &
                resposta3

    logical*2 simulating, firstDay
    
    real*4 HS(nn), DVS(nn), LARMAX(nn), NFF(nn), LAI(nn), GCROP(nn)
    
    real*4 STVG(nn),STRP(nn),STEG(nn)
    
    real*4 TTEM, TTVG, TTRP, TTEG, LARMAX
    real*4 PMAX, E, FC1, alfa, FT,
    
    real*4 DREM(nn), DRVG(nn), DRRP(nn), DREG(nn)
    
    real*4 RWLVG(nn), RWSTG(nn), RWSOG(nn)
    
    real*4 WLVG(nn), WSTG(nn), WSOG(nn)
    
    real*4 SODAY(nn), TOTALSO(nn), TOTALG(nn), PARCIAL(nn)
    
    real*4 PESODIA(nn), PESO1(nn), RENDkghaMS(nn), RENDkgha13(nn), &
            RENDscha13(nn),  SFCOLD(nn), SFHEAT(nn)
            
    real*4 fNT
end module
