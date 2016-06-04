
module SEED
    
    integer*4, parameter :: nn = 36500
        
    real*4 tmed(nn), tmin(nn), tmax(nn), radSol(nn) ; integer*2 year(nn), day(nn)
    integer*2 initYear, lastYear, initDay ; integer*4 i, ioErr 
    
    integer*4 CO2, resposta1, grupoMaturacao, cultivar,  populacao, &
                resposta3,  TBRUE, TO1RUE, TO2RUE, TMRUE,  DAYLC, AN
    character nome*14
    logical*2 simulating, firstDay, vegetative, reprodutive, grainfill
    
    real*4 HS(nn), DVS(nn), LARMAX(nn), NFF(nn), LAI(nn), GCROP(nn), & 
            RUE(nn), PAR(nn), KDF(nn), PSTPAR(nn), fNT, DRLV(nn), DVSPI, DVSF
    
    real*4 STVG(nn),STRP(nn),STEG(nn)
    
    real*4 TTEM, TTVG, TTRP, TTEG, TCMIN, TCOT, TCMAX 
    real*4 PMAX, E, FC1, FC, alfa, FT,
    
    real*4 DREM(nn), DRVG(nn), DRRP(nn), DREG(nn), NCOLD(nn), GEADA, &
            DAS(nn), MAXSTD (nn), 
    
    real*4  RWLVG(nn), RWSTG(nn), DST(nn), GST(nn)
    real*4  RWSOG(nn), WSOG(nn), RESERVA
    real*4  FSH, FLV, FST, FSO, DLV(nn), DRLV(nn), WLVG(nn), &
            WSTG(nn), WRTG(nn), RWTRG(nn)
    
    real*4 SLA(nn), ASLA, SSGA, BSLA, CSLA, DSLA, RLAI(nn),GLAI(nn), & 
            DLA(nn), LALOSS,NSLLV
    
    real*4 CTT(nn), COLDTT(nn)
    real*4 TFERT(nn), NTFERT(nn), TMM(nn)
    
    real*4 SOCF, SODAY(nn), TOTALSO(nn), TOTALG(nn), PARCIAL(nn), & 
            STRILE(nn), SOFILL(nn) 
    
    real*4 PESODIA(nn), PESO1(nn), PESOG(nn), RENDkghaMS(nn), & 
            RENDkgha13(nn), RENDscha13(nn),  SFCOLD(nn), SFHEAT(nn), & 
            SPFERT, TOTALG, 
            
end module
