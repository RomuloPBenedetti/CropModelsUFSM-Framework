!***********************************************************************
!Valores dos coeficientes genéticos-específicos para as dez cultivares
!de gladíolo e para cultivares de ciclo precoce, intemediário I
! intermediário II e tardio.
!
! Genotype specific coefficients of the ten gladiolus cultivars
! and of early, intermediate I, intermediate II, and late
! cultivars. 
!***********************************************************************

subroutine FARMING1 ()
    use SEED

    rMaxS = 0.0892 ; rMaxV = 0.0187 ; rMaxH = 0.0225
    rMaxF = 0.0708 ; LARMax = 0.1597 ; CF = 1.01

    return
end

subroutine FARMING2 ()
    use SEED

    rMaxS = 0.1066 ; rMaxV = 0.0170 ; rMaxH = 0.0221
    rMaxF = 0.0640 ; LARMax = 0.1913 ; CF = 0.0

    return
end

subroutine FARMING3 ()
    use SEED

    rMaxS = 0.0857 ; rMaxV = 0.0190 ; rMaxH = 0.0225
    rMaxF = 0.0628 ; LARMax = 0.1853 ; CF = 0.59

    return
end

subroutine FARMING4 ()
    use SEED

    rMaxS = 0.1045 ; rMaxV = 0.0174 ; rMaxH = 0.0171
    rMaxF = 0.0722 ; LARMax = 0.1780 ; CF=0.56

    return
end

subroutine FARMING5 ()
    use SEED

    rMaxS=0.0951 ; rMaxV=0.0175 ; rMaxH=0.0241
    rMaxF=0.0699 ; LARMax=0.1931 ; CF=0.10

    return
end

subroutine FARMING6 ()
    use SEED

    rMaxS = 0.0886 ; rMaxV = 0.0168 ; rMaxH = 0.0229
    rMaxF = 0.0726 ; LARMax = 0.1738 ; CF=0.76

    return
end

subroutine FARMING7  ()
    use SEED

    rMaxS = 0.0985 ; rMaxV = 0.0158 ; rMaxH = 0.0256
    rMaxF = 0.0563 ; LARMax = 0.1736 ; CF= 0.75

    return
end

subroutine FARMING8 ()
    use SEED

    rMaxS = 0.0747 ; rMaxV = 0.0160 ; rMaxH = 0.0203
    rMaxF = 0.0851 ; LARMax = 0.1509 ; CF = 0.83

    return
end

subroutine FARMING9 ()
    use SEED

    rMaxS = 0.0870 ; rMaxV = 0.0157 ; rMaxH = 0.0188
    rMaxF = 0.0653 ; LARMax = 0.1429 ; CF = 1.55

    return
end

subroutine FARMING10 ()
    use SEED

    rMaxS = 0.0896 ; rMaxV = 0.0135 ; rMaxH = 0.0244
    rMaxF = 0.0627 ; LARMax = 0.1666 ; CF = 2.42

    return
end

subroutine CYCLE1 ()
    use SEED

    rMaxS = 0.0938 ; rMaxV = 0.0182 ; rMaxH = 0.0224
    rMaxF = 0.0659 ; LARMax = 0.1788 ; CF = 0.80

    return
end

subroutine CYCLE2 ()
    use SEED

    rMaxS = 0.0961 ; rMaxV = 0.0172 ; rMaxH = 0.0214
    rMaxF = 0.0716 ; LARMax = 0.1816 ; CF = 0.47

    return
end

subroutine CYCLE3 ()
    use SEED

    rMaxS = 0.0867 ; rMaxV = 0.0158 ; rMaxH = 0.0216
    rMaxF = 0.0689 ; LARMax = 0.1558 ; CF = 1.04

    return
end

subroutine CYCLE4 ()
    use SEED

    rMaxS = 0.0896 ; rMaxV = 0.0135 ; rMaxH = 0.0244
    rMaxF = 0.0627 ; LARMax = 0.1666 ; CF = 2.42

    return
end
