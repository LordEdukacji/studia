files = dir('*.csv');

for file = files'
    filename = file.name;

    data = get_data(filename);

    plot_original(data, filename);

    print(filename(1:end-4), '-dpng');
end

%%
plotting('slavkovsky_stit.csv');
%%
plotting('8.csv');
%%
plotting('las.csv');
%%
plotting('Skiby.csv');
%%
plotting('owczarnia.csv');
%%

function data = get_data(filename)
    data = readtable(filename);

    data = renamevars(data, ["Var1", "Var2"], ["x", "y"]);
end

function plot_original(data, filename)
    figure;
    plot(data, "x", "y");
    title(strcat("Oryginalne dane - ", filename));
    xlabel("Dystans [m]");
    ylabel("Wysokość [m]");
end

function v = vandermonde_matrix(xs)
    n = length(xs) - 1;

    v = zeros(n+1, n+1);

    for i = 1:n+1
        for j = 1:n+1
            v(i,j) = vpa(xs(i) ^ (n + 1 - j), 128) ;
        end
    end
end

function polynomial_interpolation(data, chosen) % for testing purposes
    points = chosen.x;
    vals = chosen.y;

    v = vandermonde_matrix(points);
    coeffs = linsolve(v, vals);

    plot(data.x, polyval(coeffs, data.x))
    plot(points, vals, 'o');
end

function lagrange_interpolation(data, chosen)
    points_original = data.x;

    points_chosen = chosen.x;
    vals_chosen = chosen.y;

    f_calculated = zeros(size(points_original));

    for index = 1:length(points_original)
        sum = 0;

        for i = 1:length(points_chosen)
            product = 1;

            for j = 1:length(points_chosen)
                if i ~= j
                    product = product * (points_original(index) - points_chosen(j)) / (points_chosen(i) - points_chosen(j));
                end
            end

            sum = sum + product * vals_chosen(i);
        end
        f_calculated(index) = sum;
    end

    plot(points_original, f_calculated);
    plot(points_chosen, vals_chosen, 'o');
end

function spline_interpolation(data, chosen)
    if chosen.x(1) ~= data.x(1)
        chosen = [data(1,:);  chosen];    % leftmost point
    end

    if chosen.x(end) ~= data.x(end)
        chosen = [chosen; data(end,:)];  % rightmost point
    end

    n = height(chosen) - 1;

    M = zeros(4*n, 4*n);
    b = zeros(4*n, 1);

    for i = 1:n
        M(i, 4*i-3) = 1;    % a_i
        b(i) = chosen.y(i); % f(x_i)
    end

    for i = 1:n
        x_i      = chosen.x(i);
        x_iplus1 = chosen.x(i+1);

        M(i+n, 4*i-3) = 1;                      % a_i
        M(i+n, 4*i-2) = (x_iplus1 - x_i);       % b_i
        M(i+n, 4*i-1) = (x_iplus1 - x_i)^2;     % c_i
        M(i+n, 4*i  ) = (x_iplus1 - x_i)^3;     % d_i

        b(i+n) = chosen.y(i+1);                 % f(x_(i+1))
    end

    for i = 2:n
        x_i      = chosen.x(i);
        x_iminus1 = chosen.x(i-1);

        M(i+2*n-1, 4*(i-1)-2) = 1;                        % b_(i-1)
        M(i+2*n-1, 4*(i-1)-1) = 2*(x_i - x_iminus1);      % c_(i-1)
        M(i+2*n-1, 4*(i-1)  ) = 3*(x_i - x_iminus1)^2;    % d_(i-1)

        M(i+2*n-1, 4*i-2) = -1;                           % b_i
    end

    for i = 2:n
        x_i      = chosen.x(i);
        x_iminus1 = chosen.x(i-1);

        M(i+3*n-2, 4*(i-1)-1) = 2;                        % c_(i-1)
        M(i+3*n-2, 4*(i-1)  ) = 6*(x_i - x_iminus1);      % d_(i-1)

        M(i+3*n-2, 4*i-1) = -2;                           % c_i
    end

    M(4*n-1, 3) = 2;

    M(4*n, 4*n-1) = 2;
    M(4*n, 4*n  ) = 6 * (chosen.x(n+1) - chosen.x(n));

    coeffs = linsolve(M, b);

    evaluated = zeros(size(data.x));

    current_x_index = 2;
    for i = 1:length(data.x)
        while data.x(i) > chosen.x(current_x_index)
            current_x_index = current_x_index + 1;
        end

        a = coeffs(4*current_x_index - 7);
        b = coeffs(4*current_x_index - 6);
        c = coeffs(4*current_x_index - 5);
        d = coeffs(4*current_x_index - 4);
        xj = chosen.x(current_x_index-1);
        evaluated(i) = evaluate_poly3(a,b,c,d,xj,data.x(i));
    end

    plot(data.x, evaluated);
    plot(chosen.x, chosen.y, 'o');
end

function y = evaluate_poly3(a,b,c,d,xj,x)
    y = a + b*(x-xj) + c*(x-xj)^2 + d*(x-xj)^3;
end

function plotting(filename)
    data = get_data(filename);

    for num_points = 5:2:15
        chosen = data(round(linspace(1,height(data),num_points)), :);
    
        figure;
        plot(data, "x", "y");
        hold on;
    
        lagrange_interpolation(data, chosen);
    
        legend('Oryginalne dane', 'Interpolacja', 'Wybrane węzły', 'Location','best');
    
        title(strcat("Metoda Lagrange'a - ", filename, ' - ', string(height(chosen)), ' węzłów'));
        xlabel("Dystans [m]");
        ylabel("Wysokość [m]");
    
        hold off;
    
        print(strcat(filename(1:end-4), '_lagrange', string(height(chosen))), '-dpng');
    end
    
    for num_points = [5, 10, 15, 25, 45, 65]
        chosen = data(round(linspace(1,height(data),num_points)), :);
    
        figure;
        plot(data, "x", "y");
        hold on;
    
        spline_interpolation(data, chosen);
    
        legend('Oryginalne dane', 'Interpolacja', 'Wybrane węzły', 'Location','best');
    
        title(strcat("Metoda f. sklejanych - ", filename, ' - ', string(height(chosen)), ' węzłów'));
        xlabel("Dystans [m]");
        ylabel("Wysokość [m]");
    
        hold off;
    
        print(strcat(filename(1:end-4), '_spline', string(height(chosen))), '-dpng');
    end
end